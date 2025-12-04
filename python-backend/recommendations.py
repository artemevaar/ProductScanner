import numpy as np
import firebase_admin
from firebase_admin import credentials, firestore
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

cred = credentials.Certificate("/Users/Arina/PycharmProjects/scannPP/productscanner.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

def fetch_data():
    try:
        products_ref = db.collection("products")
        products_docs = products_ref.stream()

        products = {}
        for doc in products_docs:
            data = doc.to_dict()
            products[data["barcode"]] = {
                "name": data.get("name", "Unknown"),
                "ingredients": data.get("ingredients", "")
            }

        ratings_ref = db.collection("user_product_rating")
        ratings_docs = ratings_ref.stream()

        user_ratings = {}
        for doc in ratings_docs:
            data = doc.to_dict()
            user_id, product_id, rating = data["userId"], data["productId"], data["rating"]
            user_ratings.setdefault(user_id, {})[product_id] = rating

        return products, user_ratings
    except Exception as e:
        print(f"Ошибка загрузки данных: {e}")
        return {}, {}

def build_vectors(products):
    product_ids = list(products.keys())
    ingredient_texts = [products[pid]["ingredients"] for pid in product_ids]
    vectorizer = TfidfVectorizer(stop_words="english")
    ingredient_vectors = vectorizer.fit_transform(ingredient_texts)
    return product_ids, ingredient_vectors, vectorizer


def get_recommendations(user_id, products, user_ratings):

    if user_id not in user_ratings:
        return [], None, None

    product_ids, ingredient_vectors, vectorizer = build_vectors(products)

    similarity_matrix = cosine_similarity(ingredient_vectors)

    similarity_matrix = np.clip(similarity_matrix, 0, 1)

    product_index = {pid: idx for idx, pid in enumerate(product_ids)}

    liked, disliked = set(), set()
    for pid, rating in user_ratings[user_id].items():
        if rating == 5 or rating == 4:
            liked.add(pid)
        elif rating == 1 or rating == 2:
            disliked.add(pid)

    if not liked and not disliked:
        return [], None, None

    scores = np.zeros(len(product_ids))
    for pid in liked:
        if pid in product_index:
            scores += similarity_matrix[product_index[pid]]
    for pid in disliked:
        if pid in product_index:
            scores -= similarity_matrix[product_index[pid]]
    for pid in user_ratings[user_id]:
        if pid in product_index:
            scores[product_index[pid]] = -np.inf

    recommended_indices = np.argsort(scores)[::-1]
    recommended_products = [product_ids[i] for i in recommended_indices if scores[i] > 0]

    return recommended_products[:100], similarity_matrix, product_index


def show_similarities(recommended_products, similarity_matrix, product_index, products):
    for pid in recommended_products:
        if pid in product_index:
            idx = product_index[pid]
            similarities = [(known_pid, similarity_matrix[idx][known_idx] * 100)
                            for known_pid, known_idx in product_index.items() if known_pid != pid]
            similarities.sort(key=lambda x: x[1], reverse=True)
            print(f"\nПродукт: {products[pid]['name']} ({pid})")
            print("ТОП-5 похожих продуктов:")
            for similar_pid, similarity_score in similarities[:5]:
                if similarity_score > 0:
                    print(
                        f"  - {products.get(similar_pid, {}).get('name', 'Unknown')} ({similar_pid}): {similarity_score:.2f}%")


def save_recommendations(user_id, recommended_products):
    try:
        db.collection("рекомендации").document(user_id).set({"products": recommended_products})
        print("Рекомендации успешно сохранены!")
    except Exception as e:
        print(f"Ошибка сохранения рекомендаций: {e}")


def main():
    products, user_ratings = fetch_data()
    user_ids = list(user_ratings.keys())

    for user_id in user_ids:
        print(f"Рекомендации для пользователя {user_id}:")
        recommended_products, similarity_matrix, product_index = get_recommendations(user_id, products, user_ratings)

        if recommended_products:
            print("Топ-100 рекомендованных продуктов:")
            for pid in recommended_products:
                print(f"{pid}: {products[pid]['name']}")
            show_similarities(recommended_products, similarity_matrix, product_index, products)
            save_recommendations(user_id, recommended_products)
        else:
            print("Не удалось сформировать рекомендации.")


if __name__ == "__main__":
    main()

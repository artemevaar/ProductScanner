import requests
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("/Users/Arina/PycharmProjects/scannPP/productscanner.json")
firebase_admin.initialize_app(cred)
db = firestore.client()


def fetch_and_store_products(page=1, max_pages=100):

    base_url = "https://world.openfoodfacts.org/api/v2/search"
    count = 0

    for p in range(page, page + max_pages):
        params = {
            "fields": "product_name,code,ingredients_text,image_url,categories_tags,categories",
            "page_size": 1000,
            "page": p
        }

        response = requests.get(base_url, params=params)

        if response.status_code != 200:
            print(f"Ошибка при загрузке страницы {p}")
            continue

        data = response.json()
        products = data.get("products", [])

        if not products:
            print(f"Нет продуктов на странице {p}")
            break

        batch = db.batch()

        for product in products:
            product_data = {
                "name": product.get("product_name", "Название не указано"),
                "barcode": product.get("code", ""),
                "ingredients": product.get("ingredients_text", "Состав не указан"),
                "imageUrl": product.get("image_url", ""),
                "categories": product.get("categories_tags", []),
                "categoriesText": product.get("categories", "Категория не указана")
            }

            barcode = product_data["barcode"]

            if barcode:
                doc_ref = db.collection("products").document(barcode)
                doc = doc_ref.get()

                if doc.exists:
                    print(f"Продукт с баркода {barcode} уже существует, пропускаем.")
                    continue

                batch.set(doc_ref, product_data)
                count += 1

        if count > 0:
            batch.commit()
            print(f"Загружено {count} новых продуктов (Страница {p})")
        else:
            print(f"Нет новых продуктов для загрузки на странице {p}")

fetch_and_store_products(page=1, max_pages=100)
print("Готово! Продукты загружены в Firestore.")

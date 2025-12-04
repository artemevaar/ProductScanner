from flask import Flask, jsonify, request
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("/Users/Arina/PycharmProjects/scannPP/productscanner.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

app = Flask(__name__)

@app.route('/ratings', methods=['GET'])
def get_ratings():

    ratings_ref = db.collection('user_product_rating')
    docs = ratings_ref.stream()

    ratings_list = []
    for doc in docs:

        ratings_list.append(doc.to_dict())

    return jsonify(ratings_list)


@app.route('/rating/<product_id>', methods=['GET'])
def get_product_rating(product_id):

    ratings_ref = db.collection('user_product_rating').where("productId", "==", product_id)
    docs = ratings_ref.stream()

    product_ratings = [doc.to_dict() for doc in docs]
    return jsonify(product_ratings)



@app.route('/rating', methods=['POST'])
def add_rating():
    data = request.json  # Получаем JSON из запроса

    if not data or "userId" not in data or "productId" not in data or "rating" not in data:
        return jsonify({"error": "Missing data"}), 400


    doc_ref = db.collection("user_product_rating").document(f"{data['userId']}_{data['productId']}")
    doc_ref.set(data, merge=True)

    return jsonify({"message": "Rating added/updated successfully"}), 201


if __name__ == '__main__':
    app.run(debug=True)


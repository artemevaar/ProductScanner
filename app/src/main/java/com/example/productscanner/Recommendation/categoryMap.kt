package com.example.productscanner.Recommendation


val categoryKeywordsMap = mapOf(
    "Все" to listOf(),
    "Молочные продукты" to listOf(
        "en:milk", "en:cheeses", "en:butter", "en:yogurts", "en:cream", "en:fermented-milk-products"
    ),
    "Сладости" to listOf(
        "en:chocolates", "en:candies", "en:desserts", "en:ice-creams", "en:biscuits", "en:pastries"
    ),
    "Мясо, птица и рыба" to listOf(
        "en:meats",
        "en:poultries",
        "en:fishes",
        "en:seafood",
        "en:processed-meats",
        "en:smoked-fish"
    ),
    "Хлеб и выпечка" to listOf(
        "en:breads", "en:bakery-products", "en:cakes", "en:pastries", "en:croissants"
    ),
    "Крупы, макароны и бобовые, орехи" to listOf(
        "en:cereals", "en:pasta", "en:legumes", "en:grains", "en:rice", "en:nuts"
    ),
    "Напитки" to listOf(
        "en:beverages", "en:juices", "en:soft-drinks", "en:waters", "en:teas", "en:coffees"
    ),
    "Соусы, специи" to listOf(
        "en:sauces", "en:spices", "en:seasonings", "en:ketchup", "en:mustard", "en:mayonnaise"
    )
)

fun filterProductsByCategory(
    products: List<Product>,
    selectedCategory: String
): List<Product> {
    if (selectedCategory == "Все") return products

    val keywords = categoryKeywordsMap[selectedCategory]?.map { it.lowercase() }
        ?: return products

    return products.filter { product ->
        product.categories.any { productCategory ->
            keywords.any { keyword ->
                productCategory.lowercase().contains(keyword)
            }
        }
    }
}



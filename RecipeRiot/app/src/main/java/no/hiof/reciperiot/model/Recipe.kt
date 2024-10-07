package no.hiof.reciperiot.model

data class Recipe(
    val id: String = "",
    val title: String = "",
    val imageURL: String = "https://www.healthylifestylesliving.com/wp-content/uploads/2015/12/placeholder-256x256.gif",
    val cookingTime: String = "",
    var favourite: Boolean = true,
    var recipe_instructions: String = "",
    var recipe_nutrition: String = "",
    var recipe_ingredients: String = "",
    var userid: String = ""

)
package no.hiof.reciperiot.data

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import no.hiof.reciperiot.model.Recipe

class RecipeRepository {
    val user = Firebase.auth.currentUser
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = firestore.collection("FavouriteMeals")
    private val loadedRecipes: MutableList<Recipe> = mutableListOf()
    val generatedRecipes: MutableList<Recipe> = mutableStateListOf()

    fun getRecipes(test: Boolean){
        val query = if (test) {
            collectionReference
                .whereEqualTo("userid", user?.uid)
                .whereEqualTo("favourite", true)
        } else {
            collectionReference
                .whereEqualTo("userid", user?.uid)
        }

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("FirestoreError", "Feil ved henting av data: ${exception.message}")
                return@addSnapshotListener
            }
            loadedRecipes.clear()
                snapshot?.documents?.forEach { documentSnapshot ->
                    val recipe = documentSnapshot.toObject(Recipe::class.java)

                    Log.d("FirestoreData", "Data fetched successfully. Number of recipes: ${loadedRecipes.size}")
                    Log.d("FirestoreData", "recipe data: ${loadedRecipes}")
                    Log.d("FirestoreData", "recipe id: ${documentSnapshot.reference.id}")

                    recipe?.let { loadedRecipes.add(it) }
                }
            }
    }

    fun loadFavourites(): List<Recipe> {
        getRecipes(true)
        return loadedRecipes
    }
    fun loadRecipes(): List<Recipe> {
        getRecipes(false)
        return loadedRecipes
    }

    fun updateRecipeFavouriteStatus(recipe: Recipe, fav: Boolean) {
        val docid = recipe.id
        val updatedRecipe = mapOf("favourite" to fav)

        if (user != null) {
            firestore.collection("FavouriteMeals")
                .document(docid)
                .set(updatedRecipe, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Recipe favourite updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error updating recipe favourite", e)
                }
        }
    }

    fun firestoreCleanup(db: FirebaseFirestore) {
        //Ikke brukt
        val query = db.collection("FavouriteMeals")
            .whereEqualTo("userid", user?.uid)
            .whereEqualTo("favourite", false)
        query.get()
            .addOnSuccessListener { documents ->
                val batch = db.batch()
                for (document in documents) {
                    Log.d(ContentValues.TAG, "$document")
                    val documentRef = db.collection("FavouriteMeals").document(document.id)
                    Log.d(ContentValues.TAG, "Deleting document with ID: ${document.id}, Data: ${document.data}")

                    batch.delete(documentRef)
                }
                batch.commit()
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Documents successfully deleted")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error deleting documents", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error getting documents", e)
            }
    }

    fun handleFirestoreAdd(recipe: Recipe) {

        val recipeadd = mapOf(
            "id" to "",
            "title" to recipe.title,
            "imageURL" to recipe.imageURL,
            "cookingTime" to recipe.cookingTime,
            "favourite" to recipe.favourite,
            "recipe_instructions" to recipe.recipe_instructions,
            "recipe_nutrition" to recipe.recipe_nutrition,
            "recipe_ingredients" to recipe.recipe_ingredients,
            "userid" to recipe.userid
        )

        firestore.collection("FavouriteMeals")
            .add(recipeadd)
            .addOnSuccessListener { documentReference ->
                val updatedRecipe = recipe.copy(id = documentReference.id)
                updateRecipeId(updatedRecipe, documentReference.id, firestore)

                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    private fun updateRecipeId(recipe: Recipe, documentId: String, db: FirebaseFirestore) {
        val updatedRecipe = mapOf("id" to documentId)

        if (user != null) {
            db.collection("FavouriteMeals")
                .document(documentId)
                .set(updatedRecipe, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Recipe ID updated successfully")
                    generatedRecipes.clear()
                    generatedRecipes.add(recipe)
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error updating recipe ID", e)
                }
        } else {
            println("No data or error")
        }
    }

    fun handleFirestoreRemove(recipe: Recipe) {
        //ikke brukt
        Log.d(ContentValues.TAG, "Before get()")
        firestore.collection("FavouriteMeals")
            .whereEqualTo("userid", user?.uid)
            .whereEqualTo("id", recipe.id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get the document ID
                    val documentId = document.id

                    // Delete the document based on the document ID
                    firestore.collection("FavouriteMeals")
                        .document(documentId)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted with ID: $documentId")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error deleting document with ID: $documentId", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error getting documents", e)
            }
        Log.d(ContentValues.TAG, "After get()")
    }
}



package no.hiof.reciperiot.ViewModels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
class ShoppingListViewModel : ViewModel() {
    var textState by  mutableStateOf("")
    var prevState by  mutableStateOf("")
    val user = Firebase.auth.currentUser


    fun getShoppingList(db: FirebaseFirestore, callback: (shoppingListContent: String) -> Unit) {
        // TODO: Ensure logged in
        val docRef = user?.let { db.collection("shoppinglist").document(it.uid) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val shoppingListContent = document.getString("shoppingListContent") ?: ""
                callback(shoppingListContent)
            } else {
                val emptyData = emptyMap<String, Any>()
                docRef?.set(emptyData)
                    ?.addOnSuccessListener {
                        callback("")
                    }
                    ?.addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error: Could not create doc", exception)
                        callback("")
                    }
            }
        }?.addOnFailureListener { exception ->
            Log.d(ContentValues.TAG, "get failed with ", exception)
            callback("")
        }
    }

    fun saveShoppinglistToDb(db: FirebaseFirestore, shoppingListContent: String) {
        // TODO: Ensure user is logged in
        if (user != null) {
            val docRef = db.collection("shoppinglist").document(user.uid)
            val data = hashMapOf(
                "shoppingListContent" to shoppingListContent
            )
            docRef.set(data)
                .addOnSuccessListener {
                    // Successfully saved the shopping list to the database
                    Log.d("ShoppingListScreen", "Shopping list saved to the database.")
                }
                .addOnFailureListener { e ->
                    Log.e("ShoppingListScreen", "Error saving shopping list to the database: $e")
                }
        } else {
            // Handle the case when the user is not logged in
            Log.e("ShoppingListScreen", "User is not logged in.")
        }
    }
    fun clearShoppingList(db: FirebaseFirestore) {
        if (user != null) {
            val docRef = db.collection("shoppinglist").document(user.uid)
            val data = hashMapOf(
                "shoppingListContent" to ""
            )
            docRef.set(data)
                .addOnSuccessListener {
                    // Successfully saved the shopping list to the database
                    Log.d("ShoppingListScreen", "Shopping list saved to the database.")
                    getShoppingList(db) { shoppingListContent ->
                        textState = shoppingListContent
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ShoppingListScreen", "Error saving shopping list to the database: $e")
                }
        } else {
            // Handle the case when the user is not logged in
            Log.e("ShoppingListScreen", "User is not logged in.")
        }
    }

}

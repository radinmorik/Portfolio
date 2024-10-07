package no.hiof.reciperiot.data

import android.content.ContentValues
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class IngredientsRepository {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    fun fetchIngredients(callback: (Map<String, Any>?) -> Unit) {
        val docRef = user?.let { db.collection("ingredients").document(it.uid) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                callback(document.data)
            } else {
                val emptyData = emptyMap<String, Any>()
                docRef.set(emptyData)
                    ?.addOnSuccessListener {
                        callback(emptyData)
                    }
                    ?.addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error: Could not create doc", exception)
                        callback(null)
                    }
            }
        }?.addOnFailureListener { exception ->
            Log.d(ContentValues.TAG, "get failed with ", exception)
            callback(null)
        }
    }

    fun deleteIngredient(ingredientName: String) {

        val docRef = user?.let { db.collection("ingredients").document(it.uid) }

        // Delete the specific field from the document
        docRef?.update(ingredientName, FieldValue.delete())
            ?.addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
            }
            ?.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting document", e)
            }
    }

    fun saveIngredientsToDb(ingredientList: List<Pair<String, Boolean>>) {
        val docRef = user?.let { db.collection("ingredients").document(it.uid) }
        val data = hashMapOf<String, Any>()
        for ((name, checked) in ingredientList) {
            data[name] = checked
        }
        if (docRef != null) {
            docRef.set(data)
                .addOnSuccessListener { docRef ->
                    Log.d(ContentValues.TAG, "DocumentSnapcshot added!")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }
    }
}
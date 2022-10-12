package com.deletech.maps.storage
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
class PreferenceManager (internal var _context:Context){
    internal var pref: SharedPreferences
    internal var editor: SharedPreferences.Editor
    internal var PRIVATE_MODE = 0
    companion object {
        private val TYPE="TYPE"
        private val PREF_NAME = "maps_preferences"
    }
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
    fun clearData() {
        editor.clear()
        editor.commit()
    }
    fun saveType(type: String){
        editor.putString(TYPE,type)
        editor.commit()
    }
    fun getType():String{
        return pref.getString(TYPE,"dennid")!!
    }


}
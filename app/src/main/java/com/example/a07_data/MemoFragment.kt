package com.example.a07_data
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a07_data.databinding.FragmentMemoBinding

/**
 * A fragment representing a list of Items.
 */

class MemoFragment : Fragment() {
    lateinit var binding : FragmentMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_memo_list, container, false)
        binding = FragmentMemoBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun editMemo(pos:Int, memo:String) {

    }

    fun delItem(id:Int) {
    }
}


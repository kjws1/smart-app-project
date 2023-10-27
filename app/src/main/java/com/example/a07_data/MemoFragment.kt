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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a07_data.databinding.EditLayoutBinding
import com.example.a07_data.databinding.FragmentMemoBinding
import com.example.a07_data.databinding.MemoBinding

/**
 * A fragment representing a list of Items.
 */

/*
    Created to store constant values

    Every method related to DB needs a table name as its argument.
    Storing commonly used constant values increases reusability
*/
internal interface DBContract {
    companion object {
        const val TABLE_NAME = "MEMO_T"
        const val COL_ID = "ID"
        const val COL_MEMO = "MEMO"
        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COL_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_MEMO + " TEXT NOT NULL)"
        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
        const val SQL_LOAD = "SELECT * FROM " + TABLE_NAME
    }
}

internal class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_FILE, null, DB_VERSION) {

    // Run once when it's first created
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DBContract.SQL_CREATE_TABLE)
    }

    // Run when `DB_VERSION` is updated
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DBContract.SQL_DROP_TABLE)
        onCreate(db)
    }

    companion object {
        const val DB_FILE = "memo_t.db"
        const val DB_VERSION = 1
    }
}

class MemoViewHolder(val adapter: MemoAdapter, val binding: MemoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    // An index that represents the item in the list
    // Starts from 0
    var itemPos = -1
    var itemId = 0

    init {
        binding.buttonDel.setOnClickListener {
            if (itemPos != -1)
            // A View Holder can't perform modification on the item. Thus it uses an adapter instead.
                adapter.delItem(itemPos)
        }

        itemView.setOnLongClickListener {
            adapter.editItem(itemPos)
            return@setOnLongClickListener true
        }
    }
}

class MemoAdapter(val fragment: MemoFragment, val datas: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var backColor: Int = 1 //Color.rgb(0xFE,0xFB, 0xE5)

    // Whether the divider is on
    var divide: Boolean = true

    // Whether the outline is on
    var line: Boolean = true

    // run by the number of items in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MemoViewHolder(
            this,
            MemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MemoViewHolder).itemPos = position
        holder.itemId = datas[position].get("id")!!.toInt()
        val binding = holder.binding
        with(binding) {
            if (line)
                memo.setBackgroundResource(R.drawable.shape_line)
            else
                memo.setBackgroundResource(R.drawable.shape_noline)
            if (divide)
                divider.visibility = View.VISIBLE
            else
                divider.visibility = View.INVISIBLE
            textMemo.setBackgroundColor(
                when (backColor) {
                    3 -> Color.rgb(0xE2, 0xF4, 0xFC)
                    2 -> Color.rgb(0xEB, 0xE3, 0xFB)
                    else -> Color.rgb(0xFE, 0xFB, 0xE5)
                }
            )
            textMemo.text = datas[position].get("memo")
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    fun delItem(itemPos: Int) {
        if (itemPos != -1) {
            fragment.delItem(datas[itemPos].get("id")?.toInt()!!)
            datas.removeAt(itemPos)
            notifyDataSetChanged()
        }
    }


    fun editItem(itemPos: Int) {
        if (itemPos != -1) {
            fragment.editMemo(itemPos, datas[itemPos].get("memo")!!)
        }
    }
}

class MemoFragment : Fragment() {
    lateinit var binding: FragmentMemoBinding
    var adapter: MemoAdapter? = null
    var itemID: Int = 0
    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_memo_list, container, false)
        binding = FragmentMemoBinding.inflate(inflater, container, false)

        val dataList = mutableListOf<MutableMap<String, String>>()

        /*
                var item = mutableMapOf<String, String>()

                item.put("id", "0")
                item.put("memo", "다음주 중간고사")

                dataList.add(item)

                item = mutableMapOf<String, String>()
                item.put("id", "1")
                item.put("memo", "오늘 엑스포")

                dataList.add(item)
        */

        /*
            Using DB to store data
        */

        dbHelper = DBHelper(activity)
        val db = dbHelper?.readableDatabase
        val cursor = db?.rawQuery(DBContract.SQL_LOAD, null, null)
        // Run until there is no record in the DB
        while (cursor?.moveToNext()!!) {
            var item = mutableMapOf<String, String>()
            item.put("id", cursor?.getInt(0)?.toString()!!)
            item.put("memo", cursor?.getString(1)!!)
            dataList.add(item)
            // itemId stores the biggest ID
            itemID = cursor?.getInt(0)!!
        }

        adapter = MemoAdapter(this@MemoFragment, dataList)
        binding.list.adapter = adapter

        // Get settings from the shared preference and apply them
        val sPreference =
            PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext!!)
        (adapter as MemoAdapter).backColor = sPreference.getString("backcolor", "1")!!.toInt()
        (adapter as MemoAdapter).divide = sPreference.getBoolean("divider", true)
        (adapter as MemoAdapter).line = sPreference.getBoolean("line", true)

        binding.fab.setOnClickListener {
            addMemo()
        }

        return binding.root
    }

    fun addMemo() {
        val dBinding = EditLayoutBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("메모 입력")
        builder.setView(dBinding.root)
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                val db = dbHelper?.writableDatabase
                val memo = dBinding.editText.text.toString()

                val item = mutableMapOf<String, String>()
                itemID++
                item.put("id", itemID.toString())
                item.put("memo", memo)
                (binding.list.adapter as MemoAdapter).datas.add(item)

                val value = ContentValues()
                value.put("id", itemID)
                value.put("memo", memo)
                db?.insert(DBContract.TABLE_NAME, null, value)


                (binding.list.adapter as MemoAdapter).notifyDataSetChanged()
            }
        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    fun editMemo(pos: Int, memo: String) {
        val dBinding = EditLayoutBinding.inflate(layoutInflater)
        dBinding.editText.setText(memo)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("메모 입력")
        builder.setView(dBinding.root)
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                val db = dbHelper?.writableDatabase
                val memo = dBinding.editText.text.toString()

                (adapter as MemoAdapter).datas[pos].put("memo", memo)

                val value = ContentValues()
                value.put("memo", memo)
                db?.update(
                    DBContract.TABLE_NAME,
                    value,
                    "id=${(adapter as MemoAdapter).datas[pos].get("id")}",
                    null
                )
            }
        })
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    fun delItem(id: Int) {
        val db = dbHelper?.writableDatabase
        db?.delete(DBContract.TABLE_NAME, "id=$id", null)
//        db?.delete(DBContract.TABLE_NAME, "id=?", arrayOf(id.toString()))
    }
}


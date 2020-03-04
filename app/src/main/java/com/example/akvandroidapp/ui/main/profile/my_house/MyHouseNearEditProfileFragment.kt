package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdCheckboxAdapter
import kotlinx.android.synthetic.main.fragment_add_ad_near.*
import javax.inject.Inject


class MyHouseNearEditProfileFragment : BaseMyHouseFragment(), AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction {

    private lateinit var checkboxAdapter: AddAdCheckboxAdapter
    private val staticNearList = mutableListOf(
        "Больница",
        "Парк",
        "Автовокзал",
        "Аптека",
        "Кинотеатр",
        "Детский сад",
        "ЦОН",
        "Школа",
        "Акимат")

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_near, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setSpanable()
        setToolbar()
        initRecyclerView()
        setObservers()
        initialState()

        fragment_add_ad_near_next_btn.visibility = View.GONE

        fragment_add_ad_near_add_chkbox.setOnClickListener {
            fragment_add_ad_near_add_chkbox.visibility = View.GONE
            fragment_add_ad_near_add_chkbox_layout.visibility = View.VISIBLE
        }

        fragment_add_ad_near_remove_chkbox_btn.setOnClickListener {
            initialState()
        }

        fragment_add_ad_near_add_chkbox_btn.setOnClickListener {
            addNewNear(fragment_add_ad_near_add_chkbox_et.text.toString().trim())
            initialState()
        }

        fragment_add_ad_near_drop_all.setOnClickListener {
            clearAllNear()
        }
    }

    private fun setSpanable(){
        fragment_add_ad_near_drop_all.setText(fragment_add_ad_near_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_near_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_near_drop_all.text.toString().length, 0)
    }

    private fun setObservers(){
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer{
            checkboxAdapter.addItems(staticNearList, it.nearByList!!, isChecked = true, isStatic = false)
        })
    }

    private fun initialState(){
        fragment_add_ad_near_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_near_add_chkbox.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        fragment_add_ad_near_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseNearEditProfileFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@MyHouseNearEditProfileFragment, this@MyHouseNearEditProfileFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewNear(near: String) {
        val items = mutableListOf<String>()
        for(item in checkboxAdapter.getList()) items.add(item.title)
        if (!items.contains(near.capitalize()) &&
            near != "")
            checkboxAdapter.addItem(near)
        fragment_add_ad_near_add_chkbox_et.setText("")
    }

    private fun clearAllNear() {
        checkboxAdapter.uncheckAll()
    }

    private fun setToolbar(){
        fragment_add_ad_near_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_near_toolbar.setNavigationOnClickListener{
            saveNears()
            findNavController().navigateUp()
        }

        fragment_add_ad_near_cancel.visibility = View.INVISIBLE
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        checkboxAdapter.isCheckItem(position, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        checkboxAdapter.removeItem(position)
    }

    private fun saveNears(){
        val nears = mutableListOf<String>()
        for (item in checkboxAdapter.getList())
            if (item.isCheked)
                nears.add(item.title)

        sessionManager.setHouseUpdateNearItem(nears, true)
    }

    override fun onDestroy() {
        saveNears()
        super.onDestroy()
    }
}
package com.example.akvandroidapp.ui.main.profile.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.adapter.MyPagerChatAdapter
import com.example.akvandroidapp.ui.main.profile.payment.adapters.PaymentHistoryAdapter
import com.example.akvandroidapp.ui.main.profile.payment.viewmodel.PaymentViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.util.ErrorHandling
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_payments.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import kotlinx.android.synthetic.main.support_main_layout.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class PaymentFragment : BasePaymentFragment()
{

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var recyclerAdapter: PaymentHistoryAdapter
    private var isChangeState: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        initRecyclerView()
        setEditIban()
        setSessionObserver()
        subscribeObservers()
        onBlogSearchOrFilter()

    }

    private fun setSessionObserver(){
        sessionManager.iban.observe(viewLifecycleOwner, Observer{
            fragment_payments_iban_et.setText(it)
        })
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
            if(dataState != null) {
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            if(viewState != null){
                recyclerAdapter.apply {

                    if (viewState.paymentHistoryField.page != 1)
                        submitList(
                            blogList = viewState.paymentHistoryField.payments
                        )
                    else
                        clearAndSubmitList(
                            blogList = viewState.paymentHistoryField.payments
                        )

                }
            }
        })
    }

    private fun handlePagination(dataState: DataState<PaymentViewState>){

        // Handle incoming data from DataState
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }

        // Check for pagination end (no more results)
        // must do this b/c server will return an ApiErrorResponse if page is not valid,
        // -> meaning there is no more data.
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){

                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                    // set query exhausted to update RecyclerView with
                    // "No more results..." list item
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    private fun setEditIban(){
        header_payments_edit_tv.setOnClickListener {
            if (isChangeState) {
                if (validateIban(fragment_payments_iban_et.text.toString().trim())) {
                    header_payments_edit_tv.text = getString(R.string.change)
                    isChangeState = false
                    fragment_payments_iban_et.isEnabled = false
                    sessionManager.setIban(fragment_payments_iban_et.text.toString())
                }
            }else{
                header_payments_edit_tv.text = getString(R.string.save)
                isChangeState = true
                fragment_payments_iban_et.isEnabled = true
            }
        }
    }

    private fun setToolbar(){
        header_payment_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_payment_toolbar.setNavigationOnClickListener{
            activity?.finish()
        }
    }

    private fun initRecyclerView(){
        fragment_payments_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@PaymentFragment.context)
            recyclerAdapter = PaymentHistoryAdapter()
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
            recyclerAdapter.clearList()
        }
    }

    private  fun resetUI(){
        fragment_payments_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
    }

    private fun validateIban(iban: String): Boolean {
        if (iban.length == 20)
            return true
        return false
    }
}

package com.example.uebungenfd.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.uebungenfd.srhtest.LoremIpsumApi
import kotlinx.coroutines.launch
import com.example.uebungenfd.srhtest.ktorHttpClient


class MainViewModel (application: Application): AndroidViewModel(application) {

    private val internalLoremIpsumText = MutableLiveData("")
    val loremIpsumText: LiveData<String> = internalLoremIpsumText

    private val api = LoremIpsumApi(ktorHttpClient)

    private var lengthValueApi = ""

    fun loadParagraphs(count: Int) {

        //launch nicht in UI feld - asynchron
        viewModelScope.launch { internalLoremIpsumText.postValue("")
            val loremIpsum = api.get(count)
            internalLoremIpsumText.postValue(loremIpsum)
        }

    }

    fun reset() {
        internalLoremIpsumText.postValue("")
    }

    fun setLengthValue() {

    }


}
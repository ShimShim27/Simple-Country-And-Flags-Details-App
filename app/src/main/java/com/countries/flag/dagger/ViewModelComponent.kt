package com.countries.flag.dagger

import com.countries.flag.ui.main.MainActivityViewModel
import dagger.Component

@Component(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun getMainActivityViewModel(): MainActivityViewModel
}
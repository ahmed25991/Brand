package com.ibrand.ui.auth.countries

import com.ibrand.models.response.CountryModel


interface CountryCLickListener { fun sendCallback( model: CountryModel) }
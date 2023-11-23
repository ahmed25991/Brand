package com.ibrand.ui.account

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.databinding.FragmentAccountBinding
import com.ibrand.models.response.UserModel
import com.ibrand.utils.Constants
import com.ibrand.utils.Constants.USER_MODEL
import com.ibrand.utils.NavigationUtil.navigateTo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(R.layout.fragment_account) {

    private val viewModel by activityViewModels<AccountViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUserName.text = if (prefs.retrieve<UserModel>(USER_MODEL)!=null){ prefs.retrieve<UserModel>(USER_MODEL)?.username }else{ getString(R.string.guest) }
        binding.btnEditAccount.setOnClickListener { this.navigateTo(R.id.account_to_edit_profile) }
    }

}
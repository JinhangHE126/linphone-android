/*
 * Copyright (c) 2010-2023 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.ui.main.history.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.UiThread
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.linphone.R
import org.linphone.core.tools.Log
import org.linphone.databinding.StartCallFragmentBinding
import androidx.navigation.fragment.findNavController
import org.linphone.ui.GenericActivity
import org.linphone.ui.main.fragment.GenericAddressPickerFragment
import org.linphone.ui.main.history.viewmodel.StartCallViewModel
import org.linphone.ui.main.model.GroupSetOrEditSubjectDialogModel
import org.linphone.ui.main.viewmodel.AbstractMainViewModel
import org.linphone.utils.DialogUtils
import org.linphone.utils.addCharacterAtPosition
import org.linphone.utils.hideKeyboard
import org.linphone.utils.removeCharacterAtPosition
import org.linphone.utils.setKeyboardInsetListener
import org.linphone.utils.showKeyboard

@UiThread
class StartCallFragment : GenericAddressPickerFragment() {
    companion object {
        private const val TAG = "[Start Call Fragment]"
    }

    private lateinit var binding: StartCallFragmentBinding

    override lateinit var viewModel: StartCallViewModel

    private lateinit var navViewModel: AbstractMainViewModel

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                viewModel.isNumpadVisible.value = false
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) { }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val actionsBottomSheetBehavior = BottomSheetBehavior.from(binding.numpadLayout.root)
            if (actionsBottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                actionsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return
            }

            Log.i("$TAG Back gesture/click detected, no bottom sheet is expanded, going back")
            isEnabled = false
            try {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } catch (ise: IllegalStateException) {
                Log.w("$TAG Can't go back: $ise")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StartCallFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[StartCallViewModel::class.java]
        navViewModel = ViewModelProvider(this)[AbstractMainViewModel::class.java]

        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.title.value = getString(R.string.bottom_navigation_phone_label)
        binding.viewModel = viewModel
        binding.navViewModel = navViewModel
        observeToastEvents(viewModel)

        binding.setBackClickListener {
            // Dialer is the main tab root; ignore back affordance
        }

        binding.setHideNumpadClickListener {
            viewModel.hideNumpad()
        }

        binding.setAskForGroupCallSubjectClickListener {
            viewModel.hideNumpad()
            showGroupCallSubjectDialog()
        }

        setupRecyclerView(binding.contactsAndSuggestionsList)
        setupBottomNavigation()

        viewModel.modelsList.observe(
            viewLifecycleOwner
        ) {
            Log.i("$TAG Contacts & suggestions list is ready with [${it.size}] items")
            adapter.submitList(it)

            attachAdapter()

            (view.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }

        viewModel.leaveFragmentEvent.observe(viewLifecycleOwner) {
            it.consume {
                // Stay on dialer tab; CallActivity will take over the call UI
                Log.i("$TAG Call started from dialer tab, staying on Phone tab")
            }
        }

        viewModel.removedCharacterAtCurrentPositionEvent.observe(viewLifecycleOwner) {
            it.consume {
                binding.searchBar.removeCharacterAtPosition()
            }
        }

        viewModel.clearSearchBarEvent.observe(viewLifecycleOwner) {
            it.consume {
                binding.searchBar.setText("")
            }
        }

        viewModel.appendDigitToSearchBarEvent.observe(viewLifecycleOwner) {
            it.consume { digit ->
                binding.searchBar.addCharacterAtPosition(digit)
            }
        }

        viewModel.requestKeyboardVisibilityChangedEvent.observe(viewLifecycleOwner) {
            it.consume { show ->
                if (show) {
                    // To automatically open keyboard
                    binding.searchBar.showKeyboard()
                } else {
                    binding.searchBar.requestFocus()
                    binding.searchBar.hideKeyboard()
                }
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.numpadLayout.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        viewModel.isNumpadVisible.observe(viewLifecycleOwner) { visible ->
            if (visible) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.defaultAccountChangedEvent.observe(viewLifecycleOwner) {
            it.consume {
                viewModel.updateGroupCallButtonVisibility()
            }
        }

        binding.root.setKeyboardInsetListener { keyboardVisible ->
            if (keyboardVisible) {
                viewModel.isNumpadVisible.value = false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    override fun onResume() {
        super.onResume()

        sharedViewModel.currentlyDisplayedFragment.value = R.id.startCallFragment
        // Phone tab always shows the numpad by default (MicroSIP-like)
        viewModel.isNumpadVisible.value = true
    }

    override fun onPause() {
        super.onPause()

        viewModel.isNumpadVisible.value = false
    }

    private fun setupBottomNavigation() {
        navViewModel.dialerSelected.value = true

        navViewModel.navigateToDialerEvent.observe(viewLifecycleOwner) {
            it.consume {
                // Already on Phone tab
            }
        }

        navViewModel.navigateToHistoryEvent.observe(viewLifecycleOwner) {
            it.consume {
                if (findNavController().currentDestination?.id == R.id.startCallFragment) {
                    findNavController().navigate(
                        StartCallFragmentDirections.actionStartCallFragmentToHistoryListFragment()
                    )
                }
            }
        }

        navViewModel.navigateToContactsEvent.observe(viewLifecycleOwner) {
            it.consume {
                if (findNavController().currentDestination?.id == R.id.startCallFragment) {
                    findNavController().navigate(
                        StartCallFragmentDirections.actionStartCallFragmentToContactsListFragment()
                    )
                }
            }
        }

        sharedViewModel.navigateToHistoryEvent.observe(viewLifecycleOwner) {
            it.consume {
                if (findNavController().currentDestination?.id == R.id.startCallFragment) {
                    findNavController().navigate(
                        StartCallFragmentDirections.actionStartCallFragmentToHistoryListFragment()
                    )
                }
            }
        }

        sharedViewModel.navigateToContactsEvent.observe(viewLifecycleOwner) {
            it.consume {
                if (findNavController().currentDestination?.id == R.id.startCallFragment) {
                    findNavController().navigate(
                        StartCallFragmentDirections.actionStartCallFragmentToContactsListFragment()
                    )
                }
            }
        }

        sharedViewModel.currentlyDisplayedFragment.observe(viewLifecycleOwner) {
            navViewModel.dialerSelected.value = it == R.id.startCallFragment
            navViewModel.callsSelected.value = it == R.id.historyListFragment
            navViewModel.contactsSelected.value = it == R.id.contactsListFragment
        }
    }

    private fun showGroupCallSubjectDialog() {
        val model = GroupSetOrEditSubjectDialogModel("", isGroupConversation = false)

        val dialog = DialogUtils.getSetOrEditGroupSubjectDialog(
            requireContext(),
            viewLifecycleOwner,
            model
        )

        model.dismissEvent.observe(viewLifecycleOwner) {
            it.consume {
                Log.i("$TAG Set group call subject cancelled")
                dialog.dismiss()
            }
        }

        model.confirmEvent.observe(viewLifecycleOwner) {
            it.consume { newSubject ->
                if (newSubject.isNotEmpty()) {
                    Log.i(
                        "$TAG Group call subject has been set to [$newSubject]"
                    )
                    viewModel.subject.value = newSubject
                    viewModel.createGroupCall()

                    dialog.currentFocus?.hideKeyboard()
                    dialog.dismiss()
                } else {
                    val message = getString(R.string.conversation_invalid_empty_subject_toast)
                    val icon = R.drawable.warning_circle
                    (requireActivity() as GenericActivity).showRedToast(message, icon)
                }
            }
        }

        Log.i("$TAG Showing dialog to set group call subject")
        dialog.show()
    }
}

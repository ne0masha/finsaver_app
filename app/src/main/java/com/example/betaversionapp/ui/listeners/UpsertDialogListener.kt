package com.example.betaversionapp.ui.listeners

import com.example.betaversionapp.data.db.entities.Transaction

interface UpsertDialogListener {
    fun onAddButtonClicked(item: Transaction)
}
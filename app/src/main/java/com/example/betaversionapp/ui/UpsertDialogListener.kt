package com.example.betaversionapp.ui

import com.example.betaversionapp.data.db.entities.Transaction

interface UpsertDialogListener {
    fun onAddButtonClicked(item: Transaction)
    // TODO: для чего нужен этот интерфейс?
}
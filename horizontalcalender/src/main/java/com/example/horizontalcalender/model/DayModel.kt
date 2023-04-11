package com.example.horizontalcalender.model

import java.util.Date

data class DayModel(
    var date: Date,
    var isSelected:Boolean = false
){
    override fun equals(other: Any?): Boolean {
            if (other !is DayModel) {
                return false
            }
            return isSelected == other.isSelected &&
                    date ==other.date
        }

        override fun hashCode(): Int {
            return date.hashCode()
        }

}
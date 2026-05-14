package com.example.ksheerasagara.data

object CowProfitData {

    // TOTAL INCOME OF A COW
    fun getCowIncome(
        cowName: String
    ): Double {

        return MilkData
            .incomeByCow(cowName)
    }

    // TOTAL EXPENSE OF A COW
    fun getCowExpense(
        cowName: String
    ): Double {

        return ExpenseData
            .getCowExpense(cowName)
    }

    // TOTAL PROFIT / LOSS
    fun getCowProfit(
        cowName: String
    ): Double {

        val income =
            getCowIncome(cowName)

        val expense =
            getCowExpense(cowName)

        return income - expense
    }

    // TOTAL MILK LITERS
    fun getCowLiters(
        cowName: String
    ): Double {

        return MilkData
            .litersByCow(cowName)
    }

    // PROFIT PER LITER
    fun getProfitPerLiter(
        cowName: String
    ): Double {

        val liters =
            getCowLiters(cowName)

        return if (liters > 0) {

            getCowProfit(cowName) / liters

        } else {

            0.0
        }
    }

    // COW STATUS
    fun getCowStatus(
        cowName: String
    ): String {

        return if (
            getCowProfit(cowName) >= 0
        ) {
            "Profit"
        } else {
            "Loss"
        }
    }
}
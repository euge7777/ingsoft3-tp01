import { create } from 'zustand'
import type { Transaction } from '../types/dashboard'

type TransactionState = {
  transactions: Transaction[],
  dailyTransactions: Transaction[],
  getMonthlyTransactions: (transactions: Transaction[]) => void,
  getDailyTransactions: (transactions: Transaction[]) => void
  setTransaction: (transaction: Transaction) => void
}

export const useTransactionStore = create<TransactionState>((set) => ({
  transactions: [],
  dailyTransactions: [],
  getMonthlyTransactions: (transactions: Transaction[]) => set({ transactions }),
  getDailyTransactions: (transactions: Transaction[]) => set({ dailyTransactions: transactions }),
  setTransaction: (transaction: Transaction) => set((state) => ({ 
    transactions: [...state.transactions, transaction],
    dailyTransactions: [...state.dailyTransactions, transaction] 
  }))
}))

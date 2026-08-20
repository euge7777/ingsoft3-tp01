import { create } from 'zustand'

interface GeneralState {
  isTransactionFormOpen: boolean,
  isCategoryFormOpen: boolean,
  openCreateTransactionModal: () => void,
  closeCreateTransactionModal: () => void,
  openCreateCategoryModal: () => void,
  closeCreateCategoryModal: () => void
}

export const useGeneralStore = create<GeneralState>((set) => ({
  isTransactionFormOpen: false,
  isCategoryFormOpen: false,
  openCreateTransactionModal: () => set({ isTransactionFormOpen: true }),
  closeCreateTransactionModal: () => set({ isTransactionFormOpen: false }),
  openCreateCategoryModal: () => set({ isCategoryFormOpen: true }),
  closeCreateCategoryModal: () => set({ isCategoryFormOpen: false }),
}))

import React, { useEffect, useState } from 'react'
import Topbar from './layout/Topbar'
import Leftbar from './layout/Leftbar'
import Rightbar from './layout/Rightbar'
import { useCategoryStore } from '../stores/useCategoryStore'
import { useGeneralStore } from '../stores/useGeneralStore'
import { useApiClient } from '../hooks/useApiClient'
import type { Category, Transaction } from '../types/dashboard'
import { useTransactionStore } from '../stores/useTransactionStore'
import CreateTransactionForm from './transactions/CreateTransactionForm'
import CreateCategoryForm from './categories/CreateCategoryForm'

interface LayoutProps {
  children: React.ReactNode
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { categories, getAllCategories } = useCategoryStore((state) => state)
  const [error, setError] = useState({
    title: false,
    amount: false,
    type: false,
    description: false,
    categoryId: false
  })
  const [categoryError, setCategoryError] = useState({
    name: false,
    color: false
  })
  const { setTransaction } = useTransactionStore((state) => state)
  const { isTransactionFormOpen, closeCreateTransactionModal, isCategoryFormOpen, closeCreateCategoryModal, openCreateCategoryModal } = useGeneralStore((state) => state)
  const { get, post } = useApiClient()
  const [transactionInput, setTransactionInput] = useState({
    title: '',
    amount: 0,
    type: '',
    description: '',
    categoryId: ''
  })
  const [categoryInput, setCategoryInput] = useState({
    name: '',
    color: '',
    isDefault: true
  })

  useEffect(() => {
    const getCategories = async () => {
      try {
        const response = await get<Category[]>('/categories')
        if (response.data) {
          getAllCategories(response.data)
        }
      } catch (error) {
        console.log(error)
      }
    }

    getCategories()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleTransactionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTransactionInput({
      ...transactionInput,
      [event.target.name]: event.target.value
    })

    if (event.target.value === '') {
      setError({
        ...error,
        [event.target.name]: true
      })
    } else {
      setError({
        ...error,
        [event.target.name]: false
      })
    }
  }

  const handleCategoryChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCategoryInput({
      ...categoryInput,
      [event.target.name]: event.target.value
    })

    if (event.target.value === '') {
      setCategoryError({
        ...categoryError,
        [event.target.name]: true
      })
    } else {
      setCategoryError({
        ...categoryError,
        [event.target.name]: false
      })
    }
  }

  const handleSelectTransactionChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setTransactionInput({
      ...transactionInput,
      [event.target.name]: event.target.value
    })
  }

  const handleSelectCategoryChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setCategoryInput({
      ...categoryInput,
      [event.target.name]: event.target.value
    })
  }

  const handleTransactionSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      const response = await post<Transaction>('/transactions', transactionInput)
      if (response.data) {
        setTransaction(response.data)
        setTransactionInput({ title: '', amount: 0, type: 'EXPENSE', description: '', categoryId: '' })
        closeCreateTransactionModal()
      }
    } catch (error) {
      console.log(error)
    }
  }

  const handleCategorySubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    try {
      const response = await post<Category>('/categories', categoryInput)
      if (response.data) {
        getAllCategories([...categories, response.data])
        setCategoryInput({ name: '', color: '', isDefault: true })
        closeCreateCategoryModal()
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <div className='relative flex h-screen bg-gray-100 overflow-auto z-10'>
      <Leftbar />

      <Topbar>
        {children}

        <Rightbar />
      </Topbar>

      {/* form create transaction dialog */}
      <CreateTransactionForm
        isOpen={isTransactionFormOpen}
        onClose={closeCreateTransactionModal}
        title='Create Transaction'
        onSubmit={handleTransactionSubmit}
        handleChange={handleTransactionChange}
        handleSelectChange={handleSelectTransactionChange}
        transactionInput={transactionInput}
        error={error}
        categories={categories}
        openCategoryModal={openCreateCategoryModal}
      />
      <CreateCategoryForm
        isOpen={isCategoryFormOpen}
        onClose={closeCreateCategoryModal}
        title='Create Category'
        onSubmit={handleCategorySubmit}
        handleChange={handleCategoryChange}
        handleSelectChange={handleSelectCategoryChange}
        categoryInput={categoryInput}
        error={categoryError}
      />
    </div>
  )
}

export default Layout

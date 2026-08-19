import React, { useEffect, useState } from 'react'
import { CgProfile } from 'react-icons/cg'
import { Link } from 'react-router-dom'
import { jwtDecode } from 'jwt-decode'
import { useApiClient } from '../../hooks/useApiClient'

interface TopbarProps {
  children: React.ReactNode
}

interface User {
  firstName: string
}

const Topbar: React.FC<TopbarProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null)
  const { get } = useApiClient()

  useEffect(() => {
    const getUser = async () => {
      try {
        const token = localStorage.getItem('token')

        if (!token) return

        const decodedToken = jwtDecode<{ sub: string }>(token)

        const response = await get<User>(
          `/users/${decodedToken.sub}/me`
        )

        if (response.data) {
          setUser(response.data)
        }
      } catch (error) {
        console.error('Error al obtener usuario:', error)
      }
    }

    getUser()
  }, [])

  return (
    <div className='flex-1 flex flex-col mb-4'>
      <header className='bg-white flex items-center justify-between px-6 py-4'>
        <h3 className='font-semibold text-sm'>
          Welcome back{user?.firstName ? `, ${user.firstName}` : ''}
        </h3>

        <Link to='/profile' className='ml-auto cursor-pointer'>
          <CgProfile size={30} />
        </Link>
      </header>

      <main className='flex-1 overflow-auto flex relative'>
        {children}
      </main>
    </div>
  )
}

export default Topbar
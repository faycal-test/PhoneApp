package com.appfiza.phoneapp.util

interface DAOMapper<E, D> {
    fun mapToDAO(domain: E): D

    fun mapToDomain(dao: D): E
}
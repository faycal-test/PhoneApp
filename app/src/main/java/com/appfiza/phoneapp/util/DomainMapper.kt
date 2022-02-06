package com.appfiza.phoneapp.util

interface DomainMapper<E, D> {
    fun mapToDomain(entity: E): D
}
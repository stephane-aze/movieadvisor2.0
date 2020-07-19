package com.master.movieadvisor.model

data class SignInModel (val email: String? =null,
                        val sex: String?,
                        val name: String,
                        val firstName: String,
                        val password:String
)
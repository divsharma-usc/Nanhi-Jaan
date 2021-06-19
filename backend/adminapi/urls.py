from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from adminapi import views

urlpatterns = [
    path('login/', views.login),
]

urlpatterns = format_suffix_patterns(urlpatterns)

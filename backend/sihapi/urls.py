from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from sihapi import views

urlpatterns = [
    path('addNew/', views.addNew),
    path('getData/',views.getData),
    path('getDataLang/',views.getDataLang),
    path('getResource/',views.getResource)
]

urlpatterns = format_suffix_patterns(urlpatterns)

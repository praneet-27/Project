from django.urls import path

from . import views

urlpatterns = [
    path('', views.home ,name='tour-home'),
    path('package1/', views.package1 ,name='tour-package1'),
    path('package2/', views.package2 ,name='tour-package2'),
    path('package3/', views.package3 ,name='tour-package3'),
    ]


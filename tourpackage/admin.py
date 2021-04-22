from django.contrib import admin
from .models import comments
from .models import rating
admin.site.register(comments)
admin.site.register(rating)

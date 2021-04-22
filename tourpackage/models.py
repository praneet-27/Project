from django.db import models

class comments(models.Model):
	package = models.IntegerField()
	comment = models.TextField()

class rating(models.Model):
	rank = models.IntegerField()
	
def __str__(self):
	return self.package
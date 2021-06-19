from django.db import models

# Create your models here.
class DiseaseInfo(models.Model):
	name = models.CharField(max_length=20)
	generalInfo = models.CharField(max_length=2000)
	status = models.CharField(max_length =5)
	language = models.CharField(max_length=20)

class FoodInfo(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	foodInfo = models.CharField(max_length=500)

class SymptomInfo(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	symptomInfo = models.CharField(max_length=500)

class PhysicalExerciseInfo(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	physicalInfo = models.CharField(max_length=500)

class MentalExerciseInfo(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	MentalInfo =  models.CharField(max_length=500)

class Attention(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	attention = models.CharField(max_length=500)

class Prevention(models.Model):

	diseaseId = models.ForeignKey('DiseaseInfo',on_delete=models.CASCADE)
	prevention = models.CharField(max_length=500)
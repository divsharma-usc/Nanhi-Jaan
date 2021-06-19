from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.decorators import api_view

from sihapi.models import DiseaseInfo, FoodInfo, SymptomInfo, PhysicalExerciseInfo, MentalExerciseInfo, Prevention, Attention

@api_view(['POST'])
def addNew(request,format=None):
    
	  if request.method == 'POST':
	  	req_obj = request.data
	  	data_obj = DiseaseInfo.objects.filter(name=req_obj['name'])
	  	if len(data_obj)>0:
	  		return Response({'msg':'Data for this Disease already exist!!'}, status=status.HTTP_400_BAD_REQUEST)
	  	try:
	  		data_obj = DiseaseInfo(name=req_obj['name'],generalInfo=req_obj['general_info'],status=req_obj['publish_status'],language=req_obj['language'])
	  		data_obj.save()
	  		if len(request.data['foods'])>0:
	  			for i in request.data['foods']:
	  				food_obj = FoodInfo(diseaseId=data_obj,foodInfo=i)
	  				food_obj.save()

	  		if len(request.data['symptoms'])>0:
	  			for i in request.data['symptoms']:
	  				symtom_obj = SymptomInfo(diseaseId=data_obj,symptomInfo=i)
	  				symtom_obj.save()
	  			
	  		if len(request.data['physical_exercises'])>0:
	  			for i in request.data['physical_exercises']:
	  				physical_obj = PhysicalExerciseInfo(diseaseId=data_obj,physicalInfo=i)
	  				physical_obj.save()
	  			
	  		if len(request.data['mental_exercises'])>0:
	  			for i in request.data['mental_exercises']:
	  				mental_obj = MentalExerciseInfo(diseaseId=data_obj,MentalInfo=i)
	  				mental_obj.save()
	  			
	  		if len(request.data['special_attentions'])>0:
	  			for i in req_obj['special_attentions']:
	  				attention_obj = Attention(diseaseId=data_obj,attention=i)
	  				attention_obj.save()
	  			
	  		if len(request.data['preventions'])>0:
	  			for i in req_obj['preventions']:
	  				prevention_obj = Prevention(diseaseId=data_obj,prevention=i)
	  				prevention_obj.save()

	  		return Response({'msg': "Data Saved Successfully"},status=status.HTTP_200_OK)
	  	
	  	except Exception as e:
	  		print(e)
	  		return Response({'msg':'Internal Server Error!!'}, status=status.HTTP_400_BAD_REQUEST)
	  	
	  	
@api_view(['GET'])
def getData(request,format=None):
	
	if request.method == 'GET':
		publish_status = request.query_params.get('publishStatus')
		res_obj = []
		if publish_status == '0' or publish_status == '1':
			objects = DiseaseInfo.objects.filter(status=publish_status).values()
			for obj in objects:
				temp_obj = {}
				temp_obj['name'] = obj['name']
				temp_obj['language'] = obj['language']
				temp_obj['publish_status'] = obj['status']
				temp_obj['object_id'] = obj['id']
				res_obj.append(temp_obj)
		
		elif publish_status == '2':
			objects = DiseaseInfo.objects.filter().values()
			for obj in objects:
				temp_obj = {}
				temp_obj['name'] = obj['name']
				temp_obj['language'] = obj['language']
				temp_obj['publish_status'] = obj['status']
				temp_obj['object_id'] = obj['id']
				res_obj.append(temp_obj)

		return Response({'data':res_obj,'length':len(res_obj)},status=status.HTTP_200_OK)

@api_view(['GET'])
def getDataLang(request,format=None):
	if request.method == 'GET':
		language = request.query_params.get('language')
		res_obj = []
		objs = DiseaseInfo.objects.filter(language=language).values()
		for obj in objs:
			temp_obj = {}
			temp_obj['name'] = obj['name']
			temp_obj['language'] = obj['language']
			temp_obj['publish_status'] = obj['status']
			temp_obj['object_id'] = obj['id']
			res_obj.append(temp_obj)

	return Response({'data':res_obj,'length':len(res_obj)},status=status.HTTP_200_OK)


@api_view(['GET'])
def getResource(request,format=None):

	if request.method == 'GET':
		obj_id = int(request.query_params.get('id'))
		obj = DiseaseInfo.objects.filter(id=obj_id).values()
		if len(obj) > 0 :
			obj =obj.first()
			res_obj = {}
			res_obj['name'] = obj['name']
			res_obj['publish_status'] = obj['status']
			res_obj['general_info'] = obj['generalInfo']
			res_obj['language'] = obj['language']
			res_obj['symptoms'] = getSymptoms(obj_id)
			res_obj['foods'] = getFoods(obj_id)
			res_obj['physical_exercises'] = getPhysicalExcercises(obj_id)
			res_obj['mental_exercises'] = getMentalExcercises(obj_id)
			res_obj['special_attentions'] = getAttentions(obj_id)
			res_obj['preventions'] = getPreventions(obj_id)
			res_obj['status_code'] = status.HTTP_200_OK
			return Response(res_obj,status=status.HTTP_200_OK)
		else:
			return Response({'status':'Not Found','status_code':status.HTTP_404_NOT_FOUND},status=status.HTTP_404_NOT_FOUND)

def getSymptoms(obj):
	res = []
	objs = SymptomInfo.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['symptomInfo'])
	return res

def getFoods(obj):
	res = []
	objs = FoodInfo.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['foodInfo'])
	return res

def getPhysicalExcercises(obj):
	res = []
	objs = PhysicalExerciseInfo.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['physicalInfo'])
	return res

def getMentalExcercises(obj):
	res = []
	objs = MentalExerciseInfo.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['MentalInfo'])
	return res


def getAttentions(obj):
	res = []
	objs = Attention.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['attention'])
	return res

def getPreventions(obj):
	res = []
	objs = Prevention.objects.filter(diseaseId=obj).values()
	for obj in objs:
		res.append(obj['prevention'])
	return res





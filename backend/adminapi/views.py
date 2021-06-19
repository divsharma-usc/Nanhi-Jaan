import jwt,json

from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.decorators import api_view
from adminapi.models import AdminProfile

@api_view(['POST'])
def login(request,format=None):
    
	  if request.method == 'POST':
	  	user_type = AdminValidate(request,request.data)
	  	if user_type:
	  		payload  = {  'user' : request.data['username'],
	  					  'role' : user_type
	  				   }
	  		jwt_token = jwt.encode(payload, "SECRET_KEY")
	  		return Response({'token':jwt_token, 'role':user_type},status=status.HTTP_200_OK)
	  	else:
	  		return Response({'msg': "Invalid credentials",'role':0},status=status.HTTP_401_UNAUTHORIZED)


def AdminValidate(self,data):
    	
        credentials = {
    		'username' : data['username'],
    		'password' : data['password']
    	}
        
        if all(credentials.values()):
            users = AdminProfile.objects.filter(username=credentials['username'])
            if len(users)>0:
                if users.first().password == credentials['password']: 
                    return True
                else:
                    return False
            else:
                return False
        return False

from django.shortcuts import render
from django.http import HttpResponse
from . models import comments
from . models import rating

from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

def sentiment(s):
    analyzer = SentimentIntensityAnalyzer()
    neg_count=0
    neg_correct=0
    pos_count = 0
    pos_correct = 0
    threshold=0.5
    vs=analyzer.polarity_scores(s)
    
    if vs['compound'] >= threshold or vs['compound'] <= -threshold:
        if vs['compound']>0:
            pos_correct +=1
        
        
    if vs['compound'] >= threshold or vs['compound'] <= -threshold:
        if vs['compound']<=0:
            neg_correct +=1
        
    if vs['compound'] <= threshold or vs['compound'] >= -threshold:
        if vs['compound']>0.4:
            pos_correct+=1
        if vs['compound']<-0.4:
            neg_correct+=1
    
    if pos_correct>neg_correct:
        return 1
    elif neg_correct>pos_correct:
        return -1
    else:
    	return 0


def home(request):
	rank_list=list(rating.objects.values_list('rank',flat=True))
	id_list=list(rating.objects.values_list('id',flat=True))
	length=len(rank_list)
	for i in range(0,3):
		for j in range(0,3-i-1):
			if(int(rank_list[j])<int(rank_list[j+1])):
				rank_list[j],rank_list[j+1]=rank_list[j+1],rank_list[j]
				id_list[j],id_list[j+1]=id_list[j+1],id_list[j]
	
	return render(request, 'tourpackage/home.html',{'rank_list':rank_list,'id_list':id_list})


def getqueryset(self):
	return comments.objects.all()

def package1(request):
	if request.method=='POST':
		if request.POST.get('content'):
			c=comments()
			c.package=2
			c.comment= request.POST.get('content')
			c.save()
			rank=sentiment(c.comment)
			orank=rating.objects.values_list('rank',flat=True)
			r = rating.objects.get(id=1) 
			r.rank=rank+orank[0]
			r.save()
			return render(request, 'tourpackage/package1.html')
	else:
		return render(request, 'tourpackage/package1.html')


def package2(request):
	if request.method=='POST':
		if request.POST.get('content'):
			c=comments()
			c.package=1
			c.comment= request.POST.get('content')
			c.save()
			rank=sentiment(c.comment)
			orank=rating.objects.values_list('rank',flat=True)
			r = rating.objects.get(id=2)
			r.rank=rank+orank[1]
			r.save()
			return render(request, 'tourpackage/package2.html')
	else:
		return render(request, 'tourpackage/package2.html')



def package3(request):
	if request.method=='POST':
		if request.POST.get('content'):
			c=comments()
			c.package=3
			c.comment= request.POST.get('content')
			c.save()
			rank=sentiment(c.comment)
			orank=rating.objects.values_list('rank',flat=True)
			r = rating.objects.get(id=3)
			r.rank=rank+orank[2]
			r.save()
			return render(request, 'tourpackage/package3.html')
	else:
		return render(request, 'tourpackage/package3.html')
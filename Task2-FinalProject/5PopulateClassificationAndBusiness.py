import os
import time
import json
from pymongo import MongoClient

my_business_collection = MongoClient("mongodb://localhost:27017/")["yelp_attempt3"]["Business2"]
my_classification_collection = MongoClient("mongodb://localhost:27017/")["yelp_attempt3"]["Classification"]

with open('/Users/nawazkh/Masters/Courses/ILS_Z534_SEARCH/dataset/business.json') as dataset:
    for line in dataset:
        data = json.loads(line)
        if 'Restaurants' in data["categories"] and data['city'] == 'Charlotte':
           my_business_collection.insert({
             "_id": data["business_id"],
             "lat": data['latitude'],
             "lon": data['longitude'],
             "name": data['name']
           })
           my_classification_collection.insert({
           "b_id": data["business_id"],
           "lat": data['latitude'],
           "lon": data['longitude'],
           "name": data['name'],
           "categories": data['categories'],
           "stars": data["stars"]
           })

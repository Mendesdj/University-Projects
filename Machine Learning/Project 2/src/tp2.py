from plot_utilities import plot_elbow, plot_classes, plot_scores
from sklearn.metrics import silhouette_score, adjusted_rand_score
from sklearn.neighbors import KNeighborsClassifier
from sklearn.mixture import GaussianMixture
from sklearn.cluster import KMeans
from sklearn.cluster import DBSCAN
import pandas as pd
import numpy as np
import math

# ---------------------------------------------------------------------------------


def lat_lot_to_3D(lat, lon):
  RADIUS = 6371
  x = np.zeros((lat.shape[0]))
  y = np.zeros((lat.shape[0]))
  z = np.zeros((lat.shape[0]))
  
  for i in range(lat.shape[0]):
    x[i] = RADIUS * math.cos(lat[i] * math.pi/180) * math.cos(lon[i] * math.pi/180)
    y[i] = RADIUS * math.cos(lat[i] * math.pi/180) * math.sin(lon[i] * math.pi/180)
    z[i] = RADIUS * math.sin(lat[i] * math.pi/180)

  return x, y, z


# ---------------------------------------------------------------------------------


file_data = pd.read_csv('tp2_data.csv')

latitude = file_data.latitude
longitude = file_data.longitude
faults = file_data.fault

x, y, z = lat_lot_to_3D(latitude, longitude)

data = np.zeros((faults.shape[0], 3))
data[:,0] = x
data[:,1] = y
data[:,2] = z

classes = np.zeros(faults.shape[0])


# ---------------------------------------------------------------------------------


def calc_matching_matrix(clusters, faults):
  
  tp, tn, fp, fn = (0, 0, 0, 0)
  
  for i in range(0, clusters.shape[0]):
    
    current_faults = faults == faults[i]
    current_labels = clusters == clusters[i]
    
    true_positives = np.logical_and(current_faults==1, current_labels==1)
    tp += np.sum(true_positives[i+1:])
    
    true_negatives = np.logical_and(current_faults==0, current_labels==0)
    tn += np.sum(true_negatives[i+1:])
    
    false_positives = np.logical_and(current_faults==0, current_labels==1)
    fp += np.sum(false_positives[i+1:])
    
    false_negatives = np.logical_and(current_faults==1, current_labels==0)
    fn += np.sum(false_negatives[i+1:])
    
  return tp, tn, fp, fn


def calc_external_indexes(clusters, faults):
  tp, tn, fp, fn = calc_matching_matrix(clusters, faults)
  n = clusters.shape[0]
  
  rand_index = (tp + tn) / (n * (n - 1) / 2)
  ARI = adjusted_rand_score(faults, clusters)
  precision = tp / (fp + tp)
  recall = tp / (fn + tp)
  F1 = 2 * (precision * recall) / (precision + recall)
  
  return rand_index, ARI, precision, recall, F1

# ---------------------------------------------------------------------------------

def k_means_iterations(data, min_k = 2, max_k = 100, step = 1):
  
  length = int((max_k - min_k) / step)
  indexes = np.arange(min_k, max_k, step = step) 
  scores = np.zeros((length, 6))
  best_k = -1
  best_score = -1
  
  for k, i in zip (indexes, range(length)):
    kmeans_model = KMeans(n_clusters = k).fit(data)
    clusters = kmeans_model.labels_    
    scores[i,0] = silhouette_score(data, clusters)
    scores[i,1:6] = calc_external_indexes(clusters, faults)
    current_score = scores[i,0] + scores[i,1] + scores[i,2] + scores[i,5]
    if current_score >= best_score:
        best_score = current_score
        best_k = k

  return scores, indexes, best_k, best_score

def GMM_components_iterations(data, min_components = 2, max_components = 100, step = 1):
  
  length = int((max_components - min_components) / step)
  indexes = np.arange(min_components, max_components, step = step)
  scores = np.zeros((length, 6))
  best_c = -1
  best_score = -1
  
  for n_comp, i in zip (indexes, range(length)):
    gmm_model = GaussianMixture(n_components = n_comp).fit(data)
    clusters = gmm_model.predict(data)
    scores[i,0] = silhouette_score(data, clusters)
    scores[i,1:6] = calc_external_indexes(clusters, faults)
    current_score = scores[i,0] + scores[i,1] + scores[i,2] + scores[i,5]
    if current_score >= best_score:
        best_score = current_score
        best_c = n_comp
    
  return scores, indexes, best_c, best_score


def DBSCAN_distance_iterations(data, min_distance = 60, max_distance = 800, step = 10):

  length = int((max_distance - min_distance) / step)
  indexes = np.arange(min_distance, max_distance, step = step)
  scores = np.zeros((length, 6))
  best_e = -1
  best_score = -1
  
  for dist, i in zip (indexes, range(length)):
    db_model = DBSCAN(eps = dist, min_samples = 4).fit(data)
    clusters = db_model.labels_    
    scores[i,0] = silhouette_score(data, clusters)
    scores[i,1:6] = calc_external_indexes(clusters, faults)
    current_score = scores[i,1] + scores[i,2] + scores[i,5]
    if current_score >= best_score:
        best_score = current_score
        best_e = dist
  
  return scores, indexes, best_e, best_score

# -----------------------------------------------------------------------------

neighbors = KNeighborsClassifier(n_neighbors = 4).fit(data, classes)
dist, _ = neighbors.kneighbors() 
k4 = dist[:, 3]
ascending = np.sort(k4)
descending = ascending[::-1]
plot_elbow(descending)

# -----------------------------------------------------------------------------

scores, indexes, best_k, bestKM = k_means_iterations(data)
plot_scores(scores, indexes, 'K', 'K-means')
print("Best K: "),
print(best_k)
print("Best Overall Score: "),
print(bestKM)

scores, indexes, best_c, bestGMM = GMM_components_iterations(data)
plot_scores(scores, indexes, 'Nº Components', 'GMM')
print("Best C: "),
print(best_c)
print("Best Overall Score: "),
print(bestGMM)

scores, indexes, best_e, bestDB = DBSCAN_distance_iterations(data)
plot_scores(scores, indexes, 'Eps', 'DBSCAN')
print("Best Eps: "),
print(best_e)
print("Best Overall Score: "),
print(bestDB)

# -----------------------------------------------------------------------------

best_model = KMeans(n_clusters = 20).fit(data)
plot_classes(best_model.labels_, longitude, latitude, "K-Means", "k = 20")

gmm = GaussianMixture(n_components = 16).fit(data)
plot_classes(gmm.predict(data), longitude, latitude, "GMM", "c = 16")

db = DBSCAN(eps = 130, min_samples = 4).fit(data)
plot_classes(db.labels_, longitude, latitude, "DBSCAN", "Eps = 130")

import matplotlib.pyplot as plt
from skimage.io import imread
import numpy as np

# ---------------------------------------------------------------------------------

def plot_classes(labels, lon, lat, algorithm, value, alpha = 0.5, edge = 'k'):
    img = imread("Mollweide_projection_SW.jpg")        
    plt.figure(figsize=(10,5),frameon=False)    
    x = lon/180*np.pi
    y = lat/180*np.pi
    ax = plt.subplot(111, projection="mollweide")
    t = ax.transData.transform(np.vstack((x,y)).T)
    clims = np.array([(-np.pi,0),(np.pi,0),(0,-np.pi/2),(0,np.pi/2)])
    lims = ax.transData.transform(clims)
    plt.close()
    plt.figure(figsize=(10,5),frameon=False)    
    plt.subplot(111)
    plt.imshow(img,zorder=0,extent=[lims[0,0],lims[1,0],lims[2,1],lims[3,1]],aspect=1)        
    x = t[:,0]
    y= t[:,1]
    nots = np.zeros(len(labels)).astype(bool)
    diffs = np.unique(labels)    
    ix = 0   
    for lab in diffs[diffs>=0]:        
      mask = labels==lab
      nots = np.logical_or(nots,mask)        
      plt.plot(x[mask], y[mask],'o', markersize=4, mew=1,zorder=1,alpha=alpha, markeredgecolor=edge)
      ix = ix+1                    
    mask = np.logical_not(nots)    
    if np.sum(mask)>0:
      plt.plot(x[mask], y[mask], '.', markersize=1, mew=1,markerfacecolor='w', markeredgecolor=edge)
    plt.axis('off')
    plt.title(algorithm + " with " + value)
    plt.savefig(algorithm + " with " + value ,bbox_inches='tight', dpi=200)

# ---------------------------------------------------------------------------------

def plot_elbow(data):
  distances = np.array(data)
  plt.figure(figsize = (10, 5), frameon = False)
  graph, = plt.plot(distances, '-r', label = "K-dist (K = 4)");
  
  plt.margins(0.03)
  plt.yticks(np.arange(0, 2400, 75), fontsize=7)
  plt.xticks(np.arange(0, 4000, 200))
  plt.grid(b = True, which = 'major', axis = 'both', lw = '1')
  plt.savefig("Elbow",bbox_inches='tight', dpi=200)
  plt.title("4-Dist Graph")
  plt.show()    

# ---------------------------------------------------------------------------------

def plot_scores(scores, indexes, parameter, algorithm):    

  scores = np.array(scores[:])
  indexes = np.array(indexes)
  
  plt.figure(figsize = (14, 4), frameon = False)
  
  silhouette, = plt.plot(indexes[:], scores[:,0], '-b', label = "Silhouette")
  rand_index, = plt.plot(indexes[:], scores[:,1], '-y', label = "Rand Index")
  ARI,        = plt.plot(indexes[:], scores[:,2], '-m', label = "ARI")
  precision,  = plt.plot(indexes[:], scores[:,3], '-k', label = "Precision")
  recall,     = plt.plot(indexes[:], scores[:,4], '-g', label = "Recall")
  F1,         = plt.plot(indexes[:], scores[:,5], '-r', label = "F1")
  
  plt.legend(handles = [silhouette, rand_index, ARI, precision, recall, F1])
  plt.grid(b = True, which = 'major', axis = 'both', lw = '1')
  
  plt.margins(0.03)
  plt.title(algorithm)
  plt.xticks(indexes, fontsize=7, rotation=90)
  plt.yticks(np.arange(-0.2,1,0.1))
  plt.ylabel('Score')
  plt.xlabel(parameter + ' value')
  plt.savefig(algorithm, bbox_inches='tight', dpi=200)
  plt.show()

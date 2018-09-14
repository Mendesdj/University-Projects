#----------------------------------------------------------------------------
# K-NN
#----------------------------------------------------------------------------


import numpy
import matplotlib.pyplot as plt
import sys
from sklearn.cross_validation import StratifiedKFold
from sklearn.neighbors import KNeighborsClassifier


#----------------------------------------------------------------------------
# Constants
#----------------------------------------------------------------------------


NUM_FEATURES = 4
FOLDS = 5
K_START_VALUE = 1
K_END_VALUE = 39
K_STEP = 2


#----------------------------------------------------------------------------
# Functions
#----------------------------------------------------------------------------


# Returns error for train and validation sets.
# The error is calculated using the Accuracy score
def calc_fold_KNN(feature, X, Y, train_index, valid_index, k):
  classifier = KNeighborsClassifier(n_neighbors = k)
  classifier.fit(X[train_index,:feature], Y[train_index])
  return (1 - classifier.score(X[train_index,:feature], Y[train_index]),
          1 - classifier.score(X[valid_index,:feature], Y[valid_index]))
  
  
# Plot the training and validation errors
def plot_KNN_Errors (err):
    
  errors = numpy.array(err)
  poly = numpy.arange(K_START_VALUE, K_END_VALUE, step = K_STEP)
  plt.figure(figsize = (6, 4), frameon = False)
  
  train_error, = plt.plot(poly[:], errors[:,0], '-b',label="Training Error");
  validation_error, = plt.plot(poly[:], errors[:,1], '-r',label="Validation Error");
  plt.legend(handles =[train_error, validation_error])
  
  plt.ylabel('Error %')
  plt.xlabel('K value')
  plt.show()


def find_best_k(X_train, Y_train):
  
  kf = StratifiedKFold(Y_train, n_folds = FOLDS)
  errors = []
  best_validation_error = sys.maxsize
  best_k = K_START_VALUE

  for current_k in range(K_START_VALUE, K_END_VALUE, K_STEP):
    training_error = 0
    validation_error = 0
    
    # We'll to 5-Fold-Cross-Validation (i.e. this loops 5 times for each).
    for training_index, validation_index in kf:
      train, validation = calc_fold_KNN(NUM_FEATURES, X_train, Y_train, training_index, validation_index, current_k)
      training_error += train
      validation_error += validation
      
    errors.append([training_error/FOLDS, validation_error/FOLDS])
    
    if (validation_error/FOLDS < best_validation_error):
      best_validation_error = validation_error/FOLDS
      best_k = current_k
      
  plot_KNN_Errors(errors)
  
  return best_k


def calc_KNN_accuracy(X_train, Y_train, X_test, Y_test, best_k, percentage = False):
  classifier = KNeighborsClassifier(n_neighbors = best_k)
  classifier.fit(X_train, Y_train)
  test_accuracy = classifier.score(X_test, Y_test)
  Y_predicted = classifier.predict(X_test)
  return Y_predicted, test_accuracy if not percentage else test_accuracy * 100
  
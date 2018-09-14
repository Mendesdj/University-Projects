#----------------------------------------------------------------------------
# Logistic Regression
#----------------------------------------------------------------------------


import numpy
import matplotlib.pyplot as plt
import sys
from sklearn.cross_validation import StratifiedKFold
from sklearn.linear_model import LogisticRegression


#----------------------------------------------------------------------------
# Constants
#----------------------------------------------------------------------------


NUM_FEATURES = 4
FOLDS = 5
ITERATIONS = 20
C_START_VALUE = 1


#----------------------------------------------------------------------------
# Functions
#----------------------------------------------------------------------------


# Returns error for train and validation sets.
# The error is calculated using the Brier score.
def calc_fold_LR(feature, X, Y, train_index, valid_index, C = 1e12):
  reg = LogisticRegression(C = C, tol = 1e-10)
  reg.fit(X[train_index,:feature], Y[train_index])
  prob = reg.predict_proba(X[:,:feature])[:,1]
  squares = (prob-Y)**2
  return numpy.mean(squares[train_index]), numpy.mean(squares[valid_index])


# Plot the training and validation errors
def plot_LR_Errors (err):

  errors = numpy.array(err)
  poly = numpy.arange(0, ITERATIONS)
  plt.figure(figsize = (6, 4), frameon = False)
  
  train_error, = plt.plot(poly[:], errors[:,0], '-b',label="Training Error");
  validation_error, = plt.plot(poly[:], errors[:,1], '-r',label="Validation Error");
  plt.legend(handles=[train_error, validation_error])
  
  plt.ylabel('Error %')
  plt.xlabel('Iteration')
  plt.show()


# Perform Cross-Validation to determine the ideal value of C
def find_best_c(X_train, Y_train):

  kf = StratifiedKFold(Y_train, n_folds = FOLDS)
  errors = []
  best_validation_error = sys.maxsize
  
  current_c = best_c = C_START_VALUE 
  
  for index in range(0, ITERATIONS):
    training_error = 0
    validation_error = 0
    
    # We'll to 5-Fold-Cross-Validation (i.e. this loops 5 times for each).
    for training_index, validation_index in kf:
      train, validation = calc_fold_LR(NUM_FEATURES, X_train, Y_train, training_index, validation_index, current_c)
      training_error += train
      validation_error += validation
      
    errors.append([training_error/FOLDS, validation_error/FOLDS])
    
    if (validation_error/FOLDS < best_validation_error):
      best_validation_error = validation_error/FOLDS
      best_c = current_c

    current_c *= 2
  
  plot_LR_Errors(errors)
  
  return best_c
  

# Calculate and return the predicted labels of X_test and the accuracy of the trained model
def calc_LR_accuracy(X_train, Y_train, X_test, Y_test, best_c, percentage = False):
  reg = LogisticRegression(C = best_c)
  reg.fit(X_train, Y_train)
  test_accuracy = reg.score(X_test, Y_test)
  Y_predicted = reg.predict(X_test)
  return Y_predicted, test_accuracy if not percentage else test_accuracy * 100


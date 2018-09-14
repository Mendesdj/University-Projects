#----------------------------------------------------------------------------
# Naive Bayes
#----------------------------------------------------------------------------


import numpy
import matplotlib.pyplot as plt
import sys
from sklearn.cross_validation import StratifiedKFold
from sklearn.neighbors import KernelDensity


#----------------------------------------------------------------------------
# Constants
#----------------------------------------------------------------------------


NUM_FEATURES = 4
FOLDS = 5
B_START_VALUE = 0.01
B_END_VALUE = 1
B_STEP = 0.02


#----------------------------------------------------------------------------
# Functions
#----------------------------------------------------------------------------


# Plot the training and validation errors
def plot_NB_Errors (err):
    
  errors = numpy.array(err)
  poly = numpy.arange(B_START_VALUE, B_END_VALUE, step = B_STEP)
  plt.figure(figsize = (6, 4), frameon = False)
  
  train_error, = plt.plot(poly[:], errors[:,0], '-b',label="Training Error");
  validation_error, = plt.plot(poly[:], errors[:,1], '-r',label="Validation Error");
  plt.legend(handles=[train_error, validation_error])
  
  plt.ylabel('Error %')
  plt.xlabel('B value')
  plt.show()
 

def calc_fold_NB (X_train, Y_train, X_test, Y_test, b):
    
  # Get indexes of the class representation on the training set.
  indexes_class_0 = numpy.where(Y_train == 0)[0]
  indexes_class_1 = numpy.where(Y_train == 1)[0]
  
  # Separate the training set by classes and features.
  # Inside class 0, split by feature.
  f1_c0 = numpy.reshape(X_train[indexes_class_0][:,0],(-1,1))
  f2_c0 = numpy.reshape(X_train[indexes_class_0][:,1],(-1,1))
  f3_c0 = numpy.reshape(X_train[indexes_class_0][:,2],(-1,1))
  f4_c0 = numpy.reshape(X_train[indexes_class_0][:,3],(-1,1))
    
  # Inside class 1, split by feature.
  f1_c1 = numpy.reshape(X_train[indexes_class_1][:,0],(-1,1))
  f2_c1 = numpy.reshape(X_train[indexes_class_1][:,1],(-1,1))
  f3_c1 = numpy.reshape(X_train[indexes_class_1][:,2],(-1,1))
  f4_c1 = numpy.reshape(X_train[indexes_class_1][:,3],(-1,1))
    
  # Split test set by feature.
  X_test_f1 = numpy.reshape(X_test[:,0],(-1,1))
  X_test_f2 = numpy.reshape(X_test[:,1],(-1,1))
  X_test_f3 = numpy.reshape(X_test[:,2],(-1,1))
  X_test_f4 = numpy.reshape(X_test[:,3],(-1,1))
    
  # Split train set by feature.
  X_train_f1 = numpy.reshape(X_train[:,0],(-1,1))
  X_train_f2 = numpy.reshape(X_train[:,1],(-1,1))
  X_train_f3 = numpy.reshape(X_train[:,2],(-1,1))
  X_train_f4 = numpy.reshape(X_train[:,3],(-1,1))
    
  # Train each Kernel Density with every feature-class combination.
  f1_c0_kde = KernelDensity(bandwidth = b).fit(f1_c0)
  # Get the log(density) evaluations for feature 1 in the test set, considering class 0.
  val_ss_f1_c0 = f1_c0_kde.score_samples(X_test_f1)
  # Get the log(density) evaluations for feature 1 in the train set, considering class 0.
  tra_ss_f1_c0 = f1_c0_kde.score_samples(X_train_f1)
    
  f2_c0_kde = KernelDensity(bandwidth = b).fit(f2_c0)
  val_ss_f2_c0 = f2_c0_kde.score_samples(X_test_f2)
  tra_ss_f2_c0 = f2_c0_kde.score_samples(X_train_f2)
  
  f3_c0_kde = KernelDensity(bandwidth = b).fit(f3_c0)
  val_ss_f3_c0 = f3_c0_kde.score_samples(X_test_f3)
  tra_ss_f3_c0 = f3_c0_kde.score_samples(X_train_f3)
    
  f4_c0_kde = KernelDensity(bandwidth = b).fit (f4_c0)
  val_ss_f4_c0 = f4_c0_kde.score_samples(X_test_f4)
  tra_ss_f4_c0 = f4_c0_kde.score_samples(X_train_f4)
    
  f1_c1_kde = KernelDensity(bandwidth = b).fit (f1_c1)
  val_ss_f1_c1 = f1_c1_kde.score_samples(X_test_f1)
  tra_ss_f1_c1 = f1_c1_kde.score_samples(X_train_f1)
    
  f2_c1_kde = KernelDensity(bandwidth = b).fit (f2_c1)
  val_ss_f2_c1 = f2_c1_kde.score_samples(X_test_f2)
  tra_ss_f2_c1 = f2_c1_kde.score_samples(X_train_f2)
    
  f3_c1_kde = KernelDensity(bandwidth = b).fit (f3_c1)
  val_ss_f3_c1 = f3_c1_kde.score_samples(X_test_f3)
  tra_ss_f3_c1 = f3_c1_kde.score_samples(X_train_f3)
    
  f4_c1_kde = KernelDensity(bandwidth = b).fit (f4_c1)
  val_ss_f4_c1 = f4_c1_kde.score_samples(X_test_f4)
  tra_ss_f4_c1 = f4_c1_kde.score_samples(X_train_f4)
    
  # Store the log(density) evaluations in a matrix for training and validation.
  val_kernels = [[val_ss_f1_c0, val_ss_f2_c0, val_ss_f3_c0, val_ss_f4_c0], [val_ss_f1_c1, val_ss_f2_c1, val_ss_f3_c1, val_ss_f4_c1]]
  tra_kernels = [[tra_ss_f1_c0, tra_ss_f2_c0, tra_ss_f3_c0, tra_ss_f4_c0], [tra_ss_f1_c1, tra_ss_f2_c1, tra_ss_f3_c1, tra_ss_f4_c1]]
    
  # Calculate the a priori probabilities for each class.
  tot_len = (indexes_class_0).shape[0] + (indexes_class_1).shape[0]
  class_0prob = numpy.log(float((indexes_class_0).shape[0]) / tot_len)
  class_1prob = numpy.log(float((indexes_class_1).shape[0]) / tot_len)
    
  # Classify our training set.
  _, train_error = get_error(class_0prob, class_1prob, tra_kernels, X_train, Y_train)
   
  # Classify our validation set.
  Y_predicted, test_error = get_error(class_0prob, class_1prob, val_kernels, X_test, Y_test)
    
  return train_error, test_error, Y_predicted


def find_best_b(X_train, Y_train):
  kf = StratifiedKFold(Y_train, n_folds = FOLDS)
  errors = []
  best_validation_error = sys.maxsize
  best_b = B_START_VALUE
  
  for b in numpy.arange(B_START_VALUE, B_END_VALUE, B_STEP):
    training_error = 0
    validation_error = 0
    
    # We'll to 5-Fold-Cross-Validation (i.e. this loops 5 times for each).
    for training_index, validation_index in kf:
      X_training = X_train[training_index]
      Y_training = Y_train[training_index]
      X_test = X_train[validation_index]
      Y_test = Y_train[validation_index]
      
      train, validation, _ = calc_fold_NB(X_training, Y_training, X_test, Y_test, b)
      training_error += train
      validation_error += validation
      
    errors.append([training_error/FOLDS, validation_error/FOLDS])
    
    if (validation_error/FOLDS < best_validation_error):
      best_validation_error = validation_error/FOLDS
      best_b = b
  
  plot_NB_Errors(errors)
  
  return best_b
  

def calc_NB_accuracy(X_train, Y_train, X_test, Y_test, best_b, percentage = False):
    _, test, Y_predicted = calc_fold_NB(X_train, Y_train, X_test, Y_test, best_b)
    return Y_predicted, 1 - test if not percentage else (1 - test) * 100

    
def classify(class_0prob, class_1prob, kernels, X_test):
  
  test_size = X_test.shape[0]
  classes = numpy.zeros(test_size)
    
  # Apply the argmax formula for each point in the test set.
  for row in range(test_size):
    class0_sum = class_0prob
    class1_sum = class_1prob
  
    for column in range(X_test.shape[1]):
      class0_sum += kernels[0][column][row]
      class1_sum += kernels[1][column][row]
      
      # Classify the point accordingly.
      if (class0_sum < class1_sum):
        classes[row] = 1
    
  return classes


def get_error (class_0prob, class_1prob, kernels, X_test, Y_test):
        
  class0 = classify(class_0prob, class_1prob, kernels, X_test)
  counter = 0
    
  # Determine the amount of mismatched evaluations between the predicted and the real labels.
  for index in range(len(class0)):
    if (class0[index] != Y_test[index]):
      counter += 1
 
  return class0, float (counter) / len(class0)

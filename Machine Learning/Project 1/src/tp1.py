from sklearn.cross_validation import train_test_split
from sklearn.utils import shuffle
import logistic_regression as lr
import numpy
import knn
import nb


#----------------------------------------------------------------------------
# Constants
#----------------------------------------------------------------------------


TEST_SIZE = 0.33


#----------------------------------------------------------------------------
# General
#----------------------------------------------------------------------------


def load_data():
  return numpy.genfromtxt('TP1-data.csv', delimiter = ',')


def standardize_features(features):
  means = numpy.mean(features, axis = 0)
  std_devs = numpy.std(features, axis = 0)
  return (features - means) / std_devs


def doMcNemarTest(Y_predict_1, Y_predict_2, Y_real):
  e01 = 0
  e10 = 0
    
  for index in range(len(Y_real)):
    correct_1 = False
    correct_2 = False
        
    if (Y_predict_1[index] == Y_real[index]): correct_1 = True
    if (Y_predict_2[index] == Y_real[index]): correct_2 = True
    if (correct_1 and correct_2 == False): e10 += 1
    if (correct_2 and correct_1 == False): e01 += 1
    
  return float ((abs(e01 - e10) - 1)**2 / (e10 + e01))


#----------------------------------------------------------------------------
# Main
#----------------------------------------------------------------------------


def main():
  numpy.set_printoptions(threshold = 5)
    
  data = load_data()
  data = shuffle(data)
  classes = data[:,4]
  features = data[:,:4]
  features = standardize_features(features)
  
  X_train, X_test, Y_train, Y_test = train_test_split(features, classes, test_size = TEST_SIZE, stratify = classes)
    
  best_c = lr.find_best_c(X_train, Y_train)
  lr_predicted, lr_accuracy = lr.calc_LR_accuracy(X_train, Y_train, X_test, Y_test, best_c, percentage = True)

  print("Best C: %i" % best_c)
  print("Accuracy: %.2f%%" % lr_accuracy)
    
  best_k = knn.find_best_k(X_train, Y_train)
  knn_predicted, knn_accuracy = knn.calc_KNN_accuracy(X_train, Y_train, X_test, Y_test, best_k, percentage = True)
  print("Best K: %i" % best_k)
  print("Accuracy: %.2f%%" % knn_accuracy)
    
  best_b = nb.find_best_b(X_train, Y_train)
  nb_predicted, nb_accuracy = nb.calc_NB_accuracy(X_train, Y_train, X_test, Y_test, best_b, percentage = True)
  print("Best B: %.2f" % best_b)
  print("Accuracy: %.2f%%" % nb_accuracy)
  
  lr_knn = doMcNemarTest (lr_predicted, knn_predicted, Y_test )
  lr_nb = doMcNemarTest (lr_predicted, nb_predicted, Y_test )
  knn_nb = doMcNemarTest (knn_predicted, nb_predicted, Y_test )
  
  print("")
  print("McNemar's test between LR and KNN: "),
  print(lr_knn)
  
  print("")
  print("McNemar's test between LR and NB: "),
  print(lr_nb)
  
  print("")
  print("McNemar's test between KNN and NBB: "),
  print(knn_nb)
  
  return best_c, lr_accuracy, best_k, knn_accuracy, best_b, nb_accuracy, lr_knn, lr_nb, knn_nb


main()
    
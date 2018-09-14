#----------------------------------------------------------------------------
# Module created to run multiple tests and get statistics for each model.
# Default number of tests is 10.
#----------------------------------------------------------------------------


import numpy
from tp1 import main


n_tests = 10

c_values = []
k_values = []
b_values = []

lr_accuracy_values = []
knn_accuracy_values = []
nb_accuracy_values = []

mcNemar_lr_knn_values = []
mcNemar_lr_nb_values = []
mcNemar_knn_nb_values = []

for index in range (n_tests):
  print("Test Number "),
  print(index+1)

  best_c, lr_accuracy, best_k, knn_accuracy, best_b, nb_accuracy, lr_knn, lr_nb, knn_nb = main()
    
  c_values.append(best_c)
  k_values.append(best_k)
  b_values.append(best_b)
  
  lr_accuracy_values.append(lr_accuracy)  
  knn_accuracy_values.append(knn_accuracy)
  nb_accuracy_values.append(nb_accuracy)

  mcNemar_lr_knn_values.append(lr_knn)
  mcNemar_lr_nb_values.append(lr_nb)
  mcNemar_knn_nb_values.append(knn_nb)
    
  
print ("-------------------------------------------------------")
print("Average Results")    
print("")
print("C Values: "),
print(c_values )
print("Average C: %i" % numpy.array(c_values).mean())
print("LR Average Accuracy: %.2f%%" % numpy.array(lr_accuracy_values).mean())

print ("")

print("K Values: "),
print(k_values )
print("Average K: %i" % numpy.array(k_values).mean())
print("KNN Average Accuracy: %.2f%%" % numpy.array(knn_accuracy_values).mean())

print ("")

print("B Values: "),
print([round(float(i), 2) for i in b_values])
print("Best B: %.2f" % numpy.array(b_values).mean())
print("NB Average Accuracy: %.2f%%" % numpy.array(nb_accuracy_values).mean())

print ("")

print("McNemar's test between LR and KNN: "),
print([round(float(i), 2) for i in mcNemar_lr_knn_values])
print("Average McNemar's score between LR and KNN: %.2f" % numpy.array(mcNemar_lr_knn_values).mean())

print("")

print("McNemar's test between LR and NB: "),
print([round(float(i), 2) for i in mcNemar_lr_nb_values])
print("Average McNemar's score between LR and NB: %.2f" % numpy.array(mcNemar_lr_nb_values).mean())

print("")

print("McNemar's test between KNN and NB: "),
print([round(float(i), 2) for i in mcNemar_knn_nb_values])
print("Average McNemar's score between KNN and NB: %.2f" % numpy.array(mcNemar_knn_nb_values).mean())
    
    
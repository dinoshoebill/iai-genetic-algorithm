# iai-genetic-algorithm
Java implementation of genetic algorithm as part of Introduction to Artificial Intelligence course at Faculty of Electrical Engineering and Computing, University of Zagreb, academic year 2022./2023.

<br />

- **initial** weights are sampled from normal distribution with deviation of **0.01**

- **sigmoid** funtion is used as transitional function

- error is calculated as **mean squared error**

- train error is printed every **2000** iterations and represents error of **best** individual

- test error prints error on train file of **best** individual

<br />

**Input arguments:**

- **'--train'** - relative path to train file

- **'--test'** - relative path to test file

- **'--nn'** - network configuration (eg. **'5s10s'**, see below)
 
- **'--popsize'** - population size
   
- **'--elitism'** - number of elite population
     
- **'--p'** - mutation probability (eg. **0.1** represents **10%**)

- **'--k'** - mutation scale

- **'--iter'** - number of algorithm iterations

- **'--in'** - number of inputs

<br />

**Selection:**

- selection operator is **fitness proportional selection**

<br />

**Crossover:**

- **arithmetic mean** of coresponding neuron/bias weights

<br />

**Mutation:**

- **Gaussian noise**, sampled from normal distribution with deviation **k**

- each weight is mutated with probability **p**

<br />

**Network configuration:**

- **input** layer contains **in** neurons and **output** layer contains **1** (these layers are **not** hidden layers)

- **'5s10s'** - number of neurons in each **hidden** layer, letter **'s'** denotes that transitional function is applied

- transitional funtion is only applied in **hidden** layers

<br />

**Train/test file:**

- **.csv** format

- **'<first_input>,\<second_input>,\<output>'** - 1st row

- **'<first_input_value>,\<second_input_value>,<output_value>'** - all other rows

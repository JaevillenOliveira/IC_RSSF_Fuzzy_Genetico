fis = readfis('pratica05');

[in,out] = getTunableSettings(fis);

x0 = (800:4:1200)';
x1 = (2:0.1:12)';
x = [x0 x1];
y = abs(sin(2*x0)./exp(x0/5)); 

plot(x0)

options = tunefisOptions("Method","ga");
options.MethodOptions.MaxGenerations = 5;
options.DistanceMetric = "norm1";

rng('default') % for reproducibility
[fisout,optimout] = tunefis(fis,[in;out],x,y,options);






















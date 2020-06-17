fis = readfis('Matlab_Bridge/ComnetUniformSetsV1');

[in,out] = getTunableSettings(fis);
in(1) = setTunable(in(1),false); % Set input 1 (rssi) as nontunable

options = tunefisOptions("Method","ga");
options.MethodOptions.MaxGenerations = 5;
options.UseParallel(true);

x0 = (-80:0.4:-40)';
x1 = (0:0.04:4)';
x2 = (0:0.12:12)';
x3 = (0:1:100)';
x = [x0 x1 x2 x3];

y = abs(sin(2*x3)./exp(x3/5));

rng('default');
[fisout,optimout] = tunefis(fis,[in;out],x,y,options);

## Beacon Signal filtering mode

#### Beaconstac Indoor Navigation SDK has 3 different signal filtering modes. Signal filtering is done to reduce noise and make the location data stable.

#### All three filtering modes vary in how fast they allow the location to update and how stable the location data is.


### 1. ARMA

This is the best choice when the user is moving. It updates the location fairly fast and provides stable location data.
This is the default mode in Beaconstac SDK.

### 2. GAUSSIAN

Provides fast location update but can be a little unstable if the user is moving fast.

### 3. RUNNING_AVERAGE

This is the best option when the device is stationary. The location data is very stable, but takes a long time to update.
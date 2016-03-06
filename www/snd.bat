SET ADDRESS=10.0.0.67
scp -l root -pw abc123 * %ADDRESS%:/var/RowanParkingPassApp/www
cd css
snd.bat %ADDRESS%
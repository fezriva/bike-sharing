
baywheels <- read.table("baywheels-station99", header = TRUE, sep = ",")
st.num <- 99

set.seed(20)
k.max <- 15
wss <- sapply(1:k.max,function(k){kmeans(baywheels[,3:4], k,)$tot.withinss})
wss
plot(1:k.max, wss,type="b", pch = 19, frame = FALSE, xlab="Number of clusters K",ylab="Total within-clusters sum of squares")

clusters <- kmeans(baywheels[,3:4],2)
str(clusters)
baywheels$Boroughs <- as.factor(clusters$cluster)
index.st <- baywheels$St_num==st.num
baywheels$Boroughs[index.st] <- NA
col <- as.numeric(baywheels$Boroughs)
table(baywheels$Boroughs)

### San Francisco

baywheels.SF <- subset(baywheels, Boroughs==2 | is.na(Boroughs))
wss.SF <- sapply(1:k.max,function(k){kmeans(baywheels.SF[,3:4], k,)$tot.withinss})
wss.SF
plot(1:k.max, wss.SF,type="b", pch = 19, frame = FALSE, xlab="Number of clusters K",ylab="Total within-clusters sum of squares")

set.seed(123)
clusters.SF <- kmeans(baywheels.SF[,3:4],5)
str(clusters.SF)
baywheels.SF$Boroughs <- as.factor(clusters.SF$cluster)
levels(baywheels.SF$Boroughs) <- c(levels(baywheels.SF$Boroughs),'0')
index.st <- baywheels.SF$St_num==st.num
baywheels.SF$Boroughs[index.st] <- '0'
col.SF <- as.numeric(baywheels.SF$Boroughs)

sum.SF <- sum(baywheels.SF$Num_trips)
sum.SF

sum.SF.1 <- sum(subset(baywheels.SF,Boroughs==1)$Num_trips)
sum.SF.2 <- sum(subset(baywheels.SF,Boroughs==2)$Num_trips)
sum.SF.3 <- sum(subset(baywheels.SF,Boroughs==3)$Num_trips)
sum.SF.4 <- sum(subset(baywheels.SF,Boroughs==4)$Num_trips)
sum.SF.5 <- sum(subset(baywheels.SF,Boroughs==5)$Num_trips)

sum.SF.1
sum.SF.2
sum.SF.3
sum.SF.4
sum.SF.5

### San Jose

baywheels.SJ <- subset(baywheels, Boroughs==1)
sum.SJ <- sum(baywheels.SJ$Num_trips)
sum.SJ

### Plotting the Map

library(RgoogleMaps)
par(pty="s")
SFMap <- plotmap("San Francisco", zoom = 11)
plotmap(lat = St_lat, lon = St_lon, col = col, data = baywheels)

plotmap(lat = St_lat, lon = St_lon, pch=20, col = col.SF, data = baywheels.SF)

palette()


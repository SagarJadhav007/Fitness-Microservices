import { Card, CardContent, Grid, Typography } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router';
import { getActivities } from '../services/api';

const ActivityList = () => {
  const [activities , setActivities] = useState([]);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try{
      const reponse = await getActivities();
      setActivities(reponse.data);
    }catch(error){
      console.error(error);
    }
  }

  useEffect(() => {
    fetchActivities();
  }, []);
  
  return (
    <Grid container spacing={2}>
      {activities.map((activity:any) => (
        <Grid container spacing={{xs: 2, md: 3}} columns={{xs: 4,sm:8,md:12}}>
          <Card sx={{cursor:'pointer'}} onClick={() => navigate(`/activities/${activity.id}`)}>
            <CardContent>
              <Typography variant="h6">{activity.type}</Typography>
              <Typography variant="body2">Duration: {activity.duration} mins</Typography>
              <Typography variant="body2">Calories Burned: {activity.caloriesBurned} kcal</Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  )
}

export default ActivityList
import { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { getActivityDetail } from '../services/api';
import { Box, Card, CardContent, Divider, Typography } from '@mui/material';

interface Activity {
  id: string;
  activityId: string;
  userId: string;
  activityType: string;
  recommendation: string;
  improvements: string[];
  suggestions: string[];
  safety: string[];
  createdAt: string;
}

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState<Activity | null>(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id!);
        setActivity(response.data); // because your backend returns the object directly
      } catch (e) {
        console.error(e);
      }
    };
    fetchActivityDetail();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box>
      <Card>
        <CardContent>
          <Typography variant="h5">Activity Detail</Typography>
          <Typography>Type: {activity.activityType}</Typography>
          <Typography>Date: {new Date(activity.createdAt).toLocaleDateString()}</Typography>
        </CardContent>
      </Card>

      <Card sx={{ mt: 2 }}>
        <CardContent>
          <Typography variant="h5">Personalized Recommendation</Typography>

          <Typography variant="h6" sx={{ mt: 1 }}>Analysis</Typography>
          <Typography>{activity.recommendation}</Typography>

          <Divider sx={{ my: 2 }} />

          <Typography variant="h6">Improvements</Typography>
          {activity.improvements?.map((imp, index) => (
            <Typography key={index}>- {imp}</Typography>
          ))}

          <Divider sx={{ my: 2 }} />

          <Typography variant="h6">Suggestions</Typography>
          {activity.suggestions?.map((suggestion, index) => (
            <Typography key={index}>- {suggestion}</Typography>
          ))}

          <Divider sx={{ my: 2 }} />

          <Typography variant="h6">Safety Tips</Typography>
          {activity.safety?.map((safe, index) => (
            <Typography key={index}>- {safe}</Typography>
          ))}
        </CardContent>
      </Card>
    </Box>
  );
};

export default ActivityDetail;

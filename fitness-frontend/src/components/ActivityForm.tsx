import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import React from "react";
import { addActivity } from "../services/api";

interface ActivityFormProps {
  onActivitiesAdded: () => void;
}

const ActivityForm: React.FC<ActivityFormProps> = ({ onActivitiesAdded }) => {
  const [activity, setActivity] = React.useState({
    type: "RUNNING",
    duration: "",
    caloriesBurned: "",
    additionalMetrics: {},
  });

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {
      await addActivity({
        ...activity,
        duration: Number(activity.duration),
        caloriesBurned: Number(activity.caloriesBurned),
      });
      onActivitiesAdded(); // ✅ correct callback
      setActivity({
        duration: "",
        type: "RUNNING",
        caloriesBurned: "",
        additionalMetrics: {},
      });
    } catch (e) {
      console.error("Failed to add activity", e);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4 }}>
      <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel>Activity Type</InputLabel>
        <Select
          value={activity.type}
          onChange={(e) =>
            setActivity({ ...activity, type: e.target.value })
          }
        >
          <MenuItem value="RUNNING">Running</MenuItem>
          <MenuItem value="CYCLING">Cycling</MenuItem>
          <MenuItem value="WALKING">Walking</MenuItem>
        </Select>
      </FormControl>

      <TextField
        fullWidth
        label="Duration (minutes)"
        type="number"
        sx={{ mb: 2 }}
        value={activity.duration}
        onChange={(e) =>
          setActivity({ ...activity, duration: e.target.value })
        }
      />

      <TextField
        fullWidth
        label="Calories Burned"
        type="number"
        sx={{ mb: 2 }}
        value={activity.caloriesBurned}
        onChange={(e) =>
          setActivity({ ...activity, caloriesBurned: e.target.value })
        }
      />

      <Button variant="contained" color="primary" type="submit">
        Add Activity
      </Button>
    </Box>
  );
};

export default ActivityForm;

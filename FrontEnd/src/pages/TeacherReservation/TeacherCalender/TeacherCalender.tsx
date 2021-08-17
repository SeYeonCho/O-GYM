import * as React from 'react';
// @ts-ignore
import { ViewState } from '@devexpress/dx-react-scheduler';
import {
  Scheduler, DayView, Appointments, MonthView, Toolbar,
  DateNavigator, ViewSwitcher, TodayButton, Resources, AppointmentTooltip,
} from '@devexpress/dx-react-scheduler-material-ui';
import { withStyles, Theme, createStyles } from '@material-ui/core';
import { indigo, blue, teal } from '@material-ui/core/colors';
import Paper from '@material-ui/core/Paper';
import { fade } from '@material-ui/core/styles/colorManipulator';
import { WithStyles } from '@material-ui/styles';
import classNames from 'clsx';
import axios from 'axios';
import jwt_decode from 'jwt-decode';
// @ts-ignore
import Inko from 'inko';
import { useEffect, useState } from 'react';

const resources = [{
  fieldName: 'location',
  title: 'Location',
  instances: [
    { id: 'Room 1', text: 'Room 1', color: indigo },
    { id: 'Room 2', text: 'Room 2', color: blue },
    { id: 'Room 3', text: 'Room 3', color: teal },
  ],
}, {
  fieldName: 'priority',
  title: 'Priority',
  instances: [
    { id: 1, text: 'High Priority', color: teal },
    { id: 2, text: 'Medium Priority', color: blue },
    { id: 3, text: 'Low Priority', color: indigo },
  ],
}];

const styles = ({ palette }: Theme) => createStyles({
  appointment: {
    borderRadius: 5,
    borderBottom: 0,
  },
  highPriorityAppointment: {
    borderLeft: `4px solid ${teal[500]}`,
  },
  mediumPriorityAppointment: {
    borderLeft: `4px solid ${blue[500]}`,
  },
  lowPriorityAppointment: {
    borderLeft: `4px solid ${indigo[500]}`,
  },
  weekEndCell: {
    backgroundColor: fade(palette.action.disabledBackground, 0.04),
    '&:hover': {
      backgroundColor: fade(palette.action.disabledBackground, 0.04),
    },
    '&:focus': {
      backgroundColor: fade(palette.action.disabledBackground, 0.04),
    },
  },
  weekEndDayScaleCell: {
    backgroundColor: fade(palette.action.disabledBackground, 0.06),
  },
  text: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  },
  content: {
    opacity: 0.7,
  },
  container: {
    width: '100%',
    lineHeight: 1.2,
    height: '100%',
  },
});

type AppointmentProps = Appointments.AppointmentProps & WithStyles<typeof styles>;
type AppointmentContentProps = Appointments.AppointmentContentProps & WithStyles<typeof styles>;
type TimeTableCellProps = MonthView.TimeTableCellProps & WithStyles<typeof styles>;
type DayScaleCellProps = MonthView.DayScaleCellProps & WithStyles<typeof styles>;

const isWeekEnd = (date: Date): boolean => date.getDay() === 0 || date.getDay() === 6;
const defaultCurrentDate = new Date();

const DayScaleCell = withStyles(styles)(({
  startDate, classes, ...restProps
}: DayScaleCellProps) => (
  <MonthView.DayScaleCell
    className={classNames({
      [classes.weekEndDayScaleCell]: isWeekEnd(startDate),
    })}
    startDate={startDate}
    {...restProps}
    
  />

));

const TimeTableCell = withStyles(styles)((
  { startDate, classes, ...restProps }: TimeTableCellProps,
) => (
  <MonthView.TimeTableCell
    className={classNames({
      [classes.weekEndCell]: isWeekEnd(startDate!),
    })}
    startDate={startDate}
    {...restProps}
    style={{
      height: '14vh'
    }}
  />
));

const Appointment = withStyles(styles)(({
  classes, data, ...restProps
}: AppointmentProps) => (
  
  <Appointments.Appointment
    {...restProps}
    className={classNames({
      [classes.highPriorityAppointment]: data.priority === 1,
      [classes.mediumPriorityAppointment]: data.priority === 2,
      [classes.lowPriorityAppointment]: data.priority === 3,
      [classes.appointment]: true,
    })}
    data={data}
    />
    
));

const AppointmentContent = withStyles(styles, { name: 'AppointmentContent' })(({
  classes, data, ...restProps
}: AppointmentContentProps) =>  {
  // let priority = 'high';
  // if (data.priority === 2) priority = 'medium';
  // if (data.priority === 3) priority = 'low';
  return (
    <Appointments.AppointmentContent {...restProps} data={data}>
      <div className={classes.container}>
        <div className={classes.text}>
          {data.title}
        </div>
        <div className={classNames(classes.text, classes.content)}>
          {`Location: ${data.location}`}
        </div>
      </div>
    </Appointments.AppointmentContent>
  );
});

const TeacherCalender = () => {
  
  const [appointments, setAppointments] = useState<{
    title: string,
    startDate: Date,
    endDate: Date,
    priority: number,
    location: string
  }[]>([]);
  
  useEffect(() => {
    const getData = async () => {

      let accessToken = localStorage.getItem('accessToken');
      
      if (accessToken) {
        let checkDate: {
          exp, email, role, nickname
        } = jwt_decode(accessToken);
        if (checkDate['nickname']) {
            let data = await axios.get(`https://i5b305.p.ssafy.io/api/pt/reservation`, {
              headers: {
                "Authorization": `Bearer ${accessToken}`
              }
            });
            let data21: {data } = await data.data;
            console.log('teacherCalender', data21.data);
          let inko = new Inko();
          
          let addArr: {
            title: string,
            startDate: Date,
            endDate: Date,
            priority: number,
            location: string
          }[] = [];
  
            data21.data.forEach(({
              description,
              email,
              nickname,
              reservationTime,
              username
            }) => {
  
              console.log(reservationTime);
              let startDate = new Date(reservationTime);
        
              let endDate = new Date(reservationTime);
              endDate.setHours(startDate.getHours()+1);
              console.log(startDate, endDate);
              addArr.push(
                {
                  title: `${nickname}님 PT - ${
                    description
                  }`,
                  startDate: startDate,
                    endDate: endDate,
                  priority: 3,
                  location: inko.ko2en(nickname) + inko.ko2en(
                    checkDate['nickname']
                  ), 
                }
              );
            
            });
          
          setAppointments(addArr);
          
        }
        
      }
    };
    getData();
  }, []);

  return (


    <Paper style={{
      height: "90vh",
    }}>
      <Scheduler
        data={appointments}
      >
        <ViewState
          defaultCurrentDate={defaultCurrentDate}
        />

        <MonthView
          dayScaleCellComponent={DayScaleCell}
          timeTableCellComponent={TimeTableCell}
        />

        <Appointments
          appointmentComponent={Appointment}
          appointmentContentComponent={AppointmentContent}
        />
        <Resources
          data={resources}
        />

        <AppointmentTooltip
          showCloseButton
        />
        <Toolbar />
        <DateNavigator />
        <TodayButton />
      </Scheduler>
    </Paper>
  );
}

export default TeacherCalender;
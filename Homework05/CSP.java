//package csp;

import java.time.LocalDate;
import java.util.*;
import java.util.List.*;
import java.time.temporal.ChronoUnit;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some unary and binary constraints
 * on the dates of each meeting.
 */
public class CSP {

    /**
     * Public interface for the CSP solver in which the number of meetings,
     * range of allowable dates for each meeting, and constraints on meeting
     * times are specified.
     * @param nMeetings The number of meetings that must be scheduled, indexed from 0 to n-1
     * @param rangeStart The start date (inclusive) of the domains of each of the n meeting-variables
     * @param rangeEnd The end date (inclusive) of the domains of each of the n meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of the n meetings,
     *         indexed by the variable they satisfy, or null if no solution exists.
     */
    public static List<LocalDate> solve (int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
        
        
        System.out.println("\n\n\nBEGINNING SOLVE PROCEDURE\n\n\n");
        
        
        ArrayList<LocalDate> solution = new ArrayList<LocalDate>();
        ArrayList<MeetingVar> meetingList = new ArrayList<MeetingVar>(); 
        
        for(int i = 0; i < nMeetings; i++) {
            
            meetingList.add(new CSP().new MeetingVar( rangeStart, rangeEnd ) );
            solution.add(null);
        }
        
        ///*
        for(int i = 0; i < nMeetings; i++) {   
            System.out.print("\n " + i + ": \n[");        
            for(LocalDate date : meetingList.get(i).domain) {
                
                System.out.print(date + ", " );
            }
            System.out.print("]\n");
        }
        //*/
        
        nodeConsistency(meetingList, constraints);
        arcConsistency(meetingList, constraints);
        
        ///*
        for(int i = 0; i < nMeetings; i++) {  
            System.out.print("\n " + i + ": \n[");        
            for(LocalDate date : meetingList.get(i).domain) {
                
                System.out.print(date + ", " );
            }
            System.out.print("]\n");
        }
        //*/
        
        return backtracking(solution, meetingList, constraints);
    }
    
    public static List<LocalDate> backtracking(List<LocalDate> solution, List<MeetingVar> meetingList, Set<DateConstraint> constraints) {
        
        boolean noneNull = true;
        int indexOfUnassigned = -1;
        for(int i = 0; i < solution.size(); i++) {
            if( solution.get(i) == null) {
                indexOfUnassigned = i;
                noneNull = false;
                break;
            }
        }
        
        if(noneNull == true) {
            return solution;
        }
        else {
            
           MeetingVar unassignedMeeting = meetingList.get(indexOfUnassigned); 
           int meetingId = indexOfUnassigned;
           
           for(LocalDate date: unassignedMeeting.domain) { //For each domain value that meeting could be set to
               
               solution.set(meetingId,date); //Possible State
               //System.out.println( isConstraintConsistent(meetingId, solution, constraints));
               if( isConstraintConsistent(meetingId, solution, constraints) ) {
                   
                   List<LocalDate> result = backtracking(new ArrayList<LocalDate>(solution), new ArrayList<MeetingVar>(meetingList), constraints);
                   
                   
                   if(result != null) {
                       
                       return result;
                   }
                   
               }
               solution.set(meetingId, null);
           }
           return null;
        }
    }
    
    
    public static void nodeConsistency(List<MeetingVar> meetingList, Set<DateConstraint> constraints) {
        
        
        
        for(DateConstraint constraint : constraints) {
            
            if(constraint.arity() == 2) {
                continue;
            }
            
            
            ArrayList<LocalDate> toRemove = new ArrayList<LocalDate>();
            MeetingVar meeting = meetingList.get(constraint.L_VAL);
            LocalDate rightDate = ( (UnaryDateConstraint) constraint).R_VAL;
            
            for(LocalDate domainDate: meeting.domain) {
            
                switch (constraint.OP) {
                    case "==": if (!domainDate.isEqual(rightDate))  toRemove.add(domainDate); break;
                    case "!=": if (domainDate.isEqual(rightDate)) toRemove.add(domainDate); break;
                    case ">":  if (domainDate.isBefore(rightDate) || domainDate.isEqual(rightDate))  toRemove.add(domainDate); break;
                    case "<":  if (domainDate.isAfter(rightDate) ||  domainDate.isEqual(rightDate)) toRemove.add(domainDate); break;
                    case ">=": if (domainDate.isBefore(rightDate)) toRemove.add(domainDate); break;
                    case "<=": if (domainDate.isAfter(rightDate))  toRemove.add(domainDate); break;
                }
            
            }

            for(int i = 0; i < toRemove.size(); i++) {
                meeting.domain.remove(toRemove.get(i));
            }
        }

    }
    
    public static void arcConsistency(List<MeetingVar> meetingList, Set<DateConstraint> constraints) {
        
        for(DateConstraint constraint : constraints) {
            
            if(constraint.arity() == 1) {
                continue;
            }
            
            //System.out.println("\n" + constraint.toString() + "\n");
            
            ArrayList<LocalDate> toRemove = new ArrayList<LocalDate>();
            MeetingVar meetingLeft = meetingList.get(constraint.L_VAL);
            MeetingVar meetingRight =  meetingList.get( ((BinaryDateConstraint) constraint).R_VAL);
 
            /*
            System.out.print("Values in Right Domain: [");
            
            for(LocalDate rightDomainDate: meetingRight.domain) {
                System.out.print(rightDomainDate.toString() + " , ");
            }
            System.out.println("]");
            //*/
 
            //
            for(LocalDate rightDomainDate: meetingRight.domain) { //Right is Acting Tail
                 boolean anyConsistent = false;
                for(LocalDate leftDomainDate: meetingLeft.domain) { //Left is Acting Head
                    
                     
                     //System.out.println("  <- " + leftDomainDate + " " + constraint.OP + " " + rightDomainDate);
            
                    switch (constraint.OP) {
                        case "==": if (leftDomainDate.isEqual(rightDomainDate))  anyConsistent = true; break;
                        case "!=": if (!leftDomainDate.isEqual(rightDomainDate))   anyConsistent = true; break;
                        case ">":  if (leftDomainDate.isAfter(rightDomainDate)) anyConsistent = true; break;
                        case "<":  if (leftDomainDate.isBefore(rightDomainDate)) anyConsistent = true; break;
                        case ">=": if (leftDomainDate.isAfter(rightDomainDate) || leftDomainDate.isEqual(rightDomainDate)) anyConsistent = true; break;
                        case "<=": if (leftDomainDate.isBefore(rightDomainDate) || leftDomainDate.isEqual(rightDomainDate))  anyConsistent = true; break;
                    }
                    //System.out.println("Any Consistent: " + anyConsistent);
                    
                }
                if(!anyConsistent) toRemove.add(rightDomainDate);
            }
            
            for(int i = 0; i < toRemove.size(); i++) {
                meetingRight.domain.remove(toRemove.get(i));
            }
            /*
            System.out.print("Right Domain After: [");
            
            for(LocalDate rightDomainDate: meetingRight.domain) {
                System.out.print(rightDomainDate.toString() + " , ");
            }
            System.out.println("]\n");
            
            
            System.out.print("Values in Left Domain: [");
            
            for(LocalDate leftDomainDate: meetingLeft.domain) {
                System.out.print(leftDomainDate.toString() + " , ");
            }
            System.out.println("]");
            //*/
            //
            for(LocalDate leftDomainDate: meetingLeft.domain) { //Left is Acting Tail
                boolean anyConsistent = false;
                for(LocalDate rightDomainDate: meetingRight.domain) { //Right is Acting Head
                    
                    //System.out.println("  -> " + leftDomainDate + " " + constraint.OP + " " + rightDomainDate);
            
                    switch (constraint.OP) {
                        case "==": if (leftDomainDate.isEqual(rightDomainDate))  anyConsistent = true; break;
                        case "!=": if (!leftDomainDate.isEqual(rightDomainDate))   anyConsistent = true; break;
                        case ">":  if (leftDomainDate.isAfter(rightDomainDate)) anyConsistent = true; break;
                        case "<":  if (leftDomainDate.isBefore(rightDomainDate)) anyConsistent = true; break;
                        case ">=": if (leftDomainDate.isAfter(rightDomainDate) ||  leftDomainDate.isEqual(rightDomainDate)) anyConsistent = true; break;
                        case "<=": if (leftDomainDate.isBefore(rightDomainDate) || leftDomainDate.isEqual(rightDomainDate)) anyConsistent = true; break;
                    }
                    //System.out.println("Any Consistent: " + anyConsistent);
                    
                    
                }
                if(!anyConsistent) toRemove.add(leftDomainDate);
            }

            for(int i = 0; i < toRemove.size(); i++) {
                meetingLeft.domain.remove(toRemove.get(i));
            }
           /*
            System.out.print("Left Domain After: [");
            
            for(LocalDate leftDomainDate: meetingLeft.domain) {
                System.out.print(leftDomainDate.toString() + " , ");
            }
            System.out.println("]");
            //*/
        }
        
    }
    
    public static boolean isConstraintConsistent(int meetingId, List<LocalDate> solution, Set<DateConstraint> constraints) {
        
        for(DateConstraint constraint : constraints) {
        
            LocalDate leftDate = getLocalDate(constraint.L_VAL,solution);
            LocalDate rightDate = null;
            if(constraint.arity() == 1)
                rightDate = ( (UnaryDateConstraint) constraint).R_VAL;
            else if(constraint.arity() == 2)
                rightDate = getLocalDate( ( (BinaryDateConstraint) constraint).R_VAL , solution);
            
            if(leftDate == null || rightDate == null) {
                continue;
            }          
            if(!leftDate.isEqual(getLocalDate(meetingId,solution)) && !rightDate.isEqual(getLocalDate(meetingId,solution))) {
                continue;
            }
            
            boolean pass = false;
            
            
            //Use get local dtae function
            
            
            switch(constraint.OP) {
                
                case "==": 
                    pass = leftDate.equals(rightDate);
                   
                    break;
                case "!=":
                    pass = !leftDate.equals(rightDate);
                    break;
                case "<":
                    pass = leftDate.compareTo(rightDate) < 0;
                    break;
                case "<=":
                    pass = leftDate.compareTo(rightDate) <= 0;
                    break;
                case ">":
                    pass = leftDate.compareTo(rightDate) > 0;
                    break;
                case ">=":
                    pass = leftDate.compareTo(rightDate) >= 0;
                    break;
                default:
                    throw new IllegalArgumentException("OP Not Defined");
            }
            
            //System.out.println(leftDate.toString() + " " + constraint.OP + " " + rightDate.toString());
            //System.out.println("Passed?: " + pass);
            
            if(!pass) {
                return false;
            }
        }
        return true;
    }
    
    public static LocalDate getLocalDate(int meetingId ,List<LocalDate> solution) {
            return solution.get(meetingId);
    }

    /*
      * Data Type of Variable that holds a domain of possible dates of a meeting
      * A meeting var can also be assigned a meeting time to e cross checked against other meeting
      */
    public class MeetingVar {
        
        
        private HashSet<LocalDate> domain;
        
        /*
          *  Converts range of dats from specification into a domain set of dates
          * 
          */
        public MeetingVar(LocalDate rangeStart, LocalDate rangeEnd) {
            
            domain = new HashSet<LocalDate>();
            
            long daysInRange = ChronoUnit.DAYS.between(rangeStart,rangeEnd) + 1;

            for(int i = 0; i < daysInRange; i++) {
                domain.add( rangeStart.plusDays(i) );
            }
            
            /*for(LocalDate ld : domain) {
                System.out.println("Date:" + ld.toString());
              }*/
            
        }
        
        
        
        /*
          for (Iterator<Integer> i = set.iterator(); i.hasNext();) {
            Integer element = i.next();
            if (element % 2 == 0) {
                i.remove();
            }
            }
          */
    }
   
}